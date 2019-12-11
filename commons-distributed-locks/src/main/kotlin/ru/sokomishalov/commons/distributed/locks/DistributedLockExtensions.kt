/**
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.sokomishalov.commons.distributed.locks

import ru.sokomishalov.commons.core.string.isNotNullOrBlank
import ru.sokomishalov.commons.core.url.HOSTNAME
import java.time.Duration
import java.time.Duration.ZERO
import java.time.ZonedDateTime.now

/**
 * @author sokomishalov
 */

/**
 * @param lockName        name of lock
 * @param lockAtLeastFor  will hold the lock for this duration, at a minimum
 * @param lockAtMostFor   will hold the lock for this duration, at a maximum
 * @param action          to execute the given [action] under distributed lock.
 * @return                the return value of the action or null if locked
 */
suspend inline fun <reified T> DistributedLockProvider.withDistributedLock(
        lockName: String,
        lockAtLeastFor: Duration = ZERO,
        lockAtMostFor: Duration = lockAtLeastFor,
        ifLockedValue: T? = null,
        ifLockedLazyValue: Lazy<T?> = lazy { ifLockedValue },
        action: () -> T
): T? {
    require(lockName.isNotNullOrBlank()) { "Lock name must be not empty" }
    require(lockAtLeastFor <= lockAtMostFor) { "Invalid lock durations" }

    val now = now()

    val lockedAtLeastUntil = now + lockAtLeastFor
    val lockedAtMostUntil = now + lockAtMostFor

    val lockInfo = DistributedLockInfo(
            lockName = lockName,
            lockedBy = HOSTNAME,
            lockedAt = now,
            lockedUntil = lockedAtMostUntil
    )

    val lockResult = tryLock(lockInfo)

    return when {
        lockResult -> try {
            action()
        } finally {
            val lockedUntilWhenReleasing = when {
                lockedAtLeastUntil.isAfter(now()) -> lockedAtLeastUntil
                else -> now()
            }
            val releaseInfo = lockInfo.copy(lockedUntil = lockedUntilWhenReleasing)
            release(releaseInfo)
        }
        else -> ifLockedLazyValue.value
    }
}