@file:Suppress("UNCHECKED_CAST", "unused")

package ru.sokomishalov.commons.spring.cache

import org.springframework.cache.CacheManager


/**
 * @author sokomishalov
 */
class CacheManagerService(
        private val caches: List<String> = emptyList(),
        private val cacheManager: CacheManager = createDefaultCacheManager(caches)
) : CacheService {

    override suspend fun <T> get(cacheName: String, key: String, orElse: suspend () -> T): T {
        val cache = cacheManager.getCache(cacheName)
        val value = cache?.get(key)?.get()

        return when {
            value != null -> value as T
            else -> {
                val orElseValue = orElse()
                cache?.put(key, orElseValue)
                orElseValue
            }
        }
    }

    override suspend fun evict(cacheName: String, key: String) {
        cacheManager.getCache(cacheName)?.evict(key)
    }
}