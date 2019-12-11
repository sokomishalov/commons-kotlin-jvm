package ru.sokomishalov.commons.cache.spring

import com.fasterxml.jackson.databind.ObjectMapper
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.core.common.unit
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import java.time.temporal.TemporalAmount
import org.springframework.cache.CacheManager as SpringCacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager as SpringConcurrentMapCacheManager

/**
 * @author sokomishalov
 */
class SpringCacheService(
        override val cacheName: String = "cache",
        override val mapper: ObjectMapper = OBJECT_MAPPER,
        private val cacheManager: SpringCacheManager = SpringConcurrentMapCacheManager(cacheName)
) : CacheService {

    companion object : Loggable

    override suspend fun getRaw(key: String): String? = cacheManager.getCache(cacheName)?.get(key)?.get() as String?

    override suspend fun putRaw(key: String, value: String) = cacheManager.getCache(cacheName)?.put(key, value).unit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = logWarn("expire() is unsupported")

    override suspend fun delete(key: String) = cacheManager.getCache(cacheName)?.evict(key).unit()

    override suspend fun findKeys(glob: String): List<String> {
        logWarn("findKeys() is unsupported")
        return emptyList()
    }

    override suspend fun deleteAll() = cacheManager.getCache(cacheName)?.clear().unit()

    // override some methods for better performance
}