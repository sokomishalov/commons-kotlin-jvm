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
@file:Suppress("UNCHECKED_CAST", "unused")

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
            CaffeineCache(it, Caffeine.newBuilder().expireAfterWrite(expireAfter).build())
        })
    }
}

fun <V> CacheManager.put(cacheName: String, key: String, value: V?) {
    getCache(cacheName)?.put(key, value)
}

fun CacheManager.evict(cacheName: String, key: String) {
    getCache(cacheName)?.evict(key)
}

fun <V> CacheManager.get(cacheName: String, key: String): V? {
    return when (val value = getCache(cacheName)?.get(key)?.get()) {
        null -> null
        else -> value as V
    }
}