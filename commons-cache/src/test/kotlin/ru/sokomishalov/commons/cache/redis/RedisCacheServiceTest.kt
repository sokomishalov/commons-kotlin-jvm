package ru.sokomishalov.commons.cache.redis

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.util.RedisTestContainer
import ru.sokomishalov.commons.cache.util.createDefaultRedisContainer
import ru.sokomishalov.commons.cache.util.createRedisClient
import ru.sokomishalov.commons.core.log.Loggable

/**
 * @author sokomishalov
 */
class RedisCacheServiceTest : AbstractCacheServiceTest() {

    companion object : Loggable {
        @get:ClassRule
        val redis: RedisTestContainer = createDefaultRedisContainer()

        @AfterClass
        @JvmStatic
        fun stop() = redis.stop()
    }

    override val cacheService: CacheService by lazy {
        redis.start()
        logInfo(redis.logs)
        RedisCacheService(client = redis.createRedisClient())
    }
}