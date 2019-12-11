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
        private val connection: StatefulRedisConnection<String, String> = client.connect()
) : CacheService {

    override suspend fun getRaw(key: String): String? = connection.reactive().get(key.addPrefix()).await()

    override suspend fun putRaw(key: String, value: String) = connection.reactive().set(key.addPrefix(), value).awaitUnit()

    override suspend fun delete(key: String) = connection.reactive().del(key.addPrefix()).awaitUnit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = connection.reactive().expire(key.addPrefix(), ttl.get(SECONDS)).awaitUnit()

    override suspend fun findKeys(glob: String): List<String> {
        val scanArgs = ScanArgs().match(glob).limit(MAX_VALUE)
        return connection.reactive().scan(scanArgs).await()?.keys?.map { it.removePrefix() } ?: emptyList()
    }

    // override some methods for better performance
}