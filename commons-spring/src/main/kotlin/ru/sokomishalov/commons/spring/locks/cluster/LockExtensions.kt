package ru.sokomishalov.commons.spring.locks.cluster

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
 * @param action          to execute the given [action] under cluster lock.
 * @return                the return value of the action or null if locked
 */
suspend inline fun <reified T> LockProvider.withClusterLock(
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

    val lockedAtLeastUntil = now.plus(lockAtLeastFor)
    val lockedAtMostUntil = now.plus(lockAtMostFor)

    val lockInfo = LockInfo(
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