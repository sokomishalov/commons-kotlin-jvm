@file:Suppress("unused")

package ru.sokomishalov.commons.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import java.time.Duration
import java.time.Duration.ofSeconds
import java.time.Period
import java.time.temporal.TemporalAmount

/**
 * @author sokomishalov
 */
interface CacheService {

    // ---------------------------------------------------------------------------------------------------------------------------------

    val mapper: ObjectMapper get() = OBJECT_MAPPER
    val cacheName: String get() = "cache"

    // ---------------------------------------------------------------------------------------------------------------------------------

    fun String.addPrefix(): String = "${cacheName}${this}"
    fun String.removePrefix(): String = removePrefix(cacheName)

    // ---------------------------------------------------------------------------------------------------------------------------------

    suspend fun getRaw(key: String): String?
    suspend fun putRaw(key: String, value: String)
    suspend fun expire(key: String, ttl: TemporalAmount)
    suspend fun delete(key: String)
    suspend fun findKeys(glob: String): List<String>

    // ---------------------------------------------------------------------------------------------------------------------------------

    suspend fun exists(key: String): Boolean {
        return getRaw(key) != null
    }

    suspend fun <T> getOne(key: String, clazz: Class<T>): T? {
        return getRaw(key)?.deserializeValue(clazz)
    }

    suspend fun <T> getList(key: String, clazz: Class<T>): List<T> {
        val collectionType = mapper.typeFactory.constructCollectionType(List::class.java, clazz)
        return getRaw(key)?.deserializeValue(collectionType) ?: emptyList()
    }

    suspend fun <T> getMap(key: String, clazz: Class<T>): Map<String, T> {
        val mapType = mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, clazz)
        return getRaw(key)?.deserializeValue(mapType) ?: emptyMap()
    }

    suspend fun <T> getFromMap(key: String, mapKey: String, clazz: Class<T>): T? {
        return getMap(key, clazz)[mapKey]
    }

    suspend fun <T> put(key: String, value: T, ttl: TemporalAmount = ofSeconds(-1)) {
        putRaw(key, value.serializeValue())
        when {
            ttl is Duration && ttl.isNegative.not() -> expire(key, ttl)
            ttl is Period && ttl.isNegative.not() -> expire(key, ttl)
        }
    }

    suspend fun findAllKeys(): List<String> {
        return findKeys("*")
    }

    suspend fun <T : Any> find(pattern: String, clazz: Class<T>): List<T> {
        return findKeys(pattern).mapNotNull { getRaw(it)?.deserializeValue(clazz) }
    }

    suspend fun delete(keys: Iterable<String>) {
        keys.forEach { delete(it) }
    }

    suspend fun deleteAll() {
        findAllKeys().forEach { delete(it) }
    }

    // ---------------------------------------------------------------------------------------------------------------------------------

    fun <T> T.serializeValue(): String = mapper.writeValueAsString(this)
    fun <T> String.deserializeValue(clazz: Class<T>): T = mapper.readValue(this, clazz)
    fun <T> String.deserializeValue(typeRef: TypeReference<T>): T = mapper.readValue(this, typeRef)
    fun <T> String.deserializeValue(javaType: JavaType): T = mapper.readValue(this, javaType)
}