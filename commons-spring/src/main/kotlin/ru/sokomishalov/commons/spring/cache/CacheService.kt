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

package ru.sokomishalov.commons.spring.cache

/**
 * @author sokomishalov
 */
interface CacheService {

    companion object {
        const val DEFAULT_CACHE = "COMMON_CACHE"
    }

    suspend fun <T> get(cacheName: String = DEFAULT_CACHE, key: String): T?

    suspend fun <T> put(cacheName: String = DEFAULT_CACHE, key: String, value: T)

    suspend fun evict(cacheName: String = DEFAULT_CACHE, key: String)

    suspend fun <T> get(cacheName: String = DEFAULT_CACHE, key: String, orElse: suspend () -> T): T {
        val value = get<T>(cacheName, key)

        return when {
            value != null -> value
            else -> {
                val orElseValue = orElse()
                put(cacheName, key, orElseValue)
                orElseValue
            }
        }
    }
}