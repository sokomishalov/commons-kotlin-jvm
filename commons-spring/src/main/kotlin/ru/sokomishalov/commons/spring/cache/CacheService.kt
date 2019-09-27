@file:Suppress("unused")

package ru.sokomishalov.commons.spring.cache

/**
 * @author sokomishalov
 */
interface CacheService {

    companion object {
        const val DEFAULT_CACHE = "DEFAULT_CACHE"
    }

    suspend fun <T> get(cacheName: String = DEFAULT_CACHE, key: String): T?

    suspend fun <T> put(cacheName: String = DEFAULT_CACHE, key: String, value: T)

    suspend fun evict(cacheName: String = DEFAULT_CACHE, key: String)

    suspend fun <T> get(cacheName: String, key: String, orElse: suspend () -> T): T {
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