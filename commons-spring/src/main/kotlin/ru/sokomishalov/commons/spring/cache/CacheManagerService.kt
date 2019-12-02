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

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import ru.sokomishalov.commons.core.common.unit


/**
 * @author sokomishalov
 */
class CacheManagerService(
        private val caches: List<String> = emptyList(),
        private val cacheManager: CacheManager = ConcurrentMapCacheManager(*caches.toTypedArray())
) : CacheService {

    private fun getCache(cacheName: String): Cache? = cacheManager.getCache(cacheName)

    override suspend fun <T> get(cacheName: String, key: String): T? = getCache(cacheName)?.get(key)?.get() as T?
    override suspend fun <T> put(cacheName: String, key: String, value: T) = getCache(cacheName)?.put(key, value).unit()
    override suspend fun evict(cacheName: String, key: String) = getCache(cacheName)?.evict(key).unit()
}