package ru.sokomishalov.commons.spring.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import java.time.Duration
import java.time.Duration.ofDays

/**
 * @author sokomishalov
 */

fun createDefaultCacheManager(cacheNames: List<String>, expireAfter: Duration = ofDays(1)): CacheManager {
    return SimpleCacheManager().apply {
        setCaches(cacheNames.map {
            CaffeineCache(it, Caffeine.newBuilder().expireAfterAccess(expireAfter).build())
        })
    }
}
