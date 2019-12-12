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

    suspend fun getRaw(key: String): ByteArray?
    suspend fun putRaw(key: String, value: ByteArray)
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

    fun <T> T.serializeValue(): ByteArray = mapper.writeValueAsBytes(this)
    fun <T> ByteArray.deserializeValue(clazz: Class<T>): T = mapper.readValue(this, clazz)
    fun <T> ByteArray.deserializeValue(typeRef: TypeReference<T>): T = mapper.readValue(this, typeRef)
    fun <T> ByteArray.deserializeValue(javaType: JavaType): T = mapper.readValue(this, javaType)
}