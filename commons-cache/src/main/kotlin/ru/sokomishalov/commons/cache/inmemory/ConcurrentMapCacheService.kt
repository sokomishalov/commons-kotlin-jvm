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
@file:Suppress("unused")

package ru.sokomishalov.commons.cache.inmemory

import com.fasterxml.jackson.databind.ObjectMapper
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.core.common.unit
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import ru.sokomishalov.commons.core.string.convertGlobToRegex
import java.time.temporal.TemporalAmount
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author sokomishalov
 */
class ConcurrentMapCacheService(
        override val cacheName: String = "cache:",
        override val mapper: ObjectMapper = OBJECT_MAPPER,
        private val map: ConcurrentMap<String, ByteArray> = ConcurrentHashMap()
) : CacheService {

    companion object : Loggable

    private fun String.addPrefix(): String = "${cacheName}${this}"
    private fun String.removePrefix(): String = removePrefix(cacheName)

    override suspend fun getRaw(key: String): ByteArray? = map[key.addPrefix()]

    override suspend fun putRaw(key: String, value: ByteArray) = map.put(key.addPrefix(), value).unit()

    override suspend fun delete(key: String) = map.remove(key.addPrefix()).unit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = logWarn("expire() is unsupported")

    override suspend fun findKeys(glob: String): List<String> = map.keys.map { it.removePrefix() }.filter { glob.convertGlobToRegex().matches(it) }


    override suspend fun deleteAll() = map.clear()

    // override some methods for better performance
}