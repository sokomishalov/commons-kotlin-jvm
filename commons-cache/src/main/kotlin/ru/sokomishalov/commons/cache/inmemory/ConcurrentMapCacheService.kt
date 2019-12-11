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
        private val map: ConcurrentMap<String, String> = ConcurrentHashMap()
) : CacheService {

    companion object : Loggable

    override suspend fun getRaw(key: String): String? = map[key.addPrefix()]

    override suspend fun putRaw(key: String, value: String) = map.put(key.addPrefix(), value).unit()

    override suspend fun delete(key: String) = map.remove(key.addPrefix()).unit()

    override suspend fun expire(key: String, ttl: TemporalAmount) = logWarn("expire() is unsupported")

    override suspend fun findKeys(glob: String): List<String> = map.keys.map { it.removePrefix() }.filter { glob.convertGlobToRegex().matches(it) }


    override suspend fun deleteAll() = map.clear()

    // override some methods for better performance
}