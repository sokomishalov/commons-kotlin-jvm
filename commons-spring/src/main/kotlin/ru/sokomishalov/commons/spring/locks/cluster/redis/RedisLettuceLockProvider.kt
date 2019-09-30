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
package ru.sokomishalov.commons.spring.locks.cluster.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.SetArgs
import io.lettuce.core.api.StatefulRedisConnection
import ru.sokomishalov.commons.core.consts.EMPTY
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.core.reactor.await
import ru.sokomishalov.commons.core.reactor.awaitUnit
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import ru.sokomishalov.commons.core.serialization.aReadValue
import ru.sokomishalov.commons.core.string.isNotNullOrBlank
import ru.sokomishalov.commons.spring.locks.cluster.LockInfo
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import java.time.Duration
import java.time.ZonedDateTime.now

class RedisLettuceLockProvider(
        private val client: RedisClient = RedisClient.create(RedisURI.create(LOCALHOST)),
        private val connection: StatefulRedisConnection<String, String> = client.connect(),
        private val keyPrefix: String = EMPTY,
        private val keyDelimiter: String = DEFAULT_KEY_DELIMITER
) : LockProvider {

    companion object {
        private const val DEFAULT_KEY_DELIMITER = ":"
    }

    override suspend fun tryLock(lockInfo: LockInfo): Boolean {
        val keyValue = lockInfo.buildKeyValue()
        val expireAfterMs = Duration.between(now(), lockInfo.lockedUntil).toMillis()

        val lockAcquired = connection
                .reactive()
                .set(keyValue.first, keyValue.second, SetArgs().nx().px(expireAfterMs))
                .await()

        return when {
            lockAcquired.isNotNullOrBlank() -> true
            else -> {
                val value = connection.reactive().get(keyValue.first).await()
                value?.deserializeValue()?.lockedUntil?.isBefore(now()) ?: true
            }
        }
    }

    override suspend fun release(lockInfo: LockInfo) {
        val keyValue = lockInfo.buildKeyValue()
        connection
                .reactive()
                .set(keyValue.first, keyValue.second, SetArgs().xx())
                .awaitUnit()
    }

    private fun LockInfo.buildKeyValue(): Pair<String, String> {
        val key = when {
            keyPrefix.isBlank() -> lockName
            else -> "$keyPrefix$keyDelimiter$lockName"
        }
        val value = OBJECT_MAPPER.writeValueAsString(this)

        return key to value
    }

    private suspend fun String.deserializeValue(): LockInfo {
        return OBJECT_MAPPER.aReadValue(this)
    }
}