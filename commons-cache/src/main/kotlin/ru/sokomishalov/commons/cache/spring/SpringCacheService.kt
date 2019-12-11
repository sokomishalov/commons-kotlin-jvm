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

    override suspend fun getRaw(key: String): ByteArray? = cacheManager.getCache(cacheName)?.get(key)?.get() as ByteArray?

    override suspend fun putRaw(key: String, value: ByteArray) = cacheManager.getCache(cacheName)?.put(key, value).unit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = logWarn("expire() is unsupported")

    override suspend fun delete(key: String) = cacheManager.getCache(cacheName)?.evict(key).unit()

    override suspend fun findKeys(glob: String): List<String> {
        logWarn("findKeys() is unsupported")
        return emptyList()
    }

    override suspend fun deleteAll() = cacheManager.getCache(cacheName)?.clear().unit()

    // override some methods for better performance
}