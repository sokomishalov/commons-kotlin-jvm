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
package ru.sokomishalov.commons.cache.redis

import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.ScanArgs
import io.lettuce.core.api.StatefulRedisConnection
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.core.reactor.await
import ru.sokomishalov.commons.core.reactor.awaitUnit
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import java.time.temporal.ChronoUnit.SECONDS
import java.time.temporal.TemporalAmount
import kotlin.Long.Companion.MAX_VALUE


/**
 * @author sokomishalov
 */
open class RedisCacheService(
        override val cacheName: String = "cache",
        override val mapper: ObjectMapper = OBJECT_MAPPER,
        private val host: String = LOCALHOST,
        private val port: Int = 6379,
        private val client: RedisClient = RedisClient.create(RedisURI.create(host, port)),
        private val connection: StatefulRedisConnection<String, ByteArray> = client.connect(StringByteArrayCodec())
) : CacheService {

    private fun String.addPrefix(): String = "${cacheName}${this}"
    private fun String.removePrefix(): String = removePrefix(cacheName)

    override suspend fun getRaw(key: String): ByteArray? = connection.reactive().get(key.addPrefix()).await()

    override suspend fun putRaw(key: String, value: ByteArray) = connection.reactive().set(key.addPrefix(), value).awaitUnit()

    override suspend fun delete(key: String) = connection.reactive().del(key.addPrefix()).awaitUnit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = connection.reactive().expire(key.addPrefix(), ttl.get(SECONDS)).awaitUnit()

    override suspend fun findKeys(glob: String): List<String> {
        val scanArgs = ScanArgs().match(glob).limit(MAX_VALUE)
        return connection.reactive().scan(scanArgs).await()?.keys?.map { it.removePrefix() } ?: emptyList()
    }

    // override some methods for better performance
}