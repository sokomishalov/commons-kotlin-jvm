@file:Suppress("UNCHECKED_CAST", "unused")

package ru.sokomishalov.commons.spring.cache

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager


/**
 * @author sokomishalov
 */
class CacheManagerService(
        private val caches: List<String> = emptyList(),
        private val cacheManager: CacheManager = createDefaultCacheManager(caches)
) : CacheService {

    private fun getCache(cacheName: String): Cache? = cacheManager.getCache(cacheName)

    override suspend fun <T> get(cacheName: String, key: String): T? {
        return getCache(cacheName)?.get(key)?.get() as T?
    }

    override suspend fun <T> put(cacheName: String, key: String, value: T) {
        getCache(cacheName)?.put(key, value)
    }

    override suspend fun evict(cacheName: String, key: String) {
        getCache(cacheName)?.evict(key)
    }
}