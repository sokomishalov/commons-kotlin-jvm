@file:Suppress("unused")

package ru.sokomishalov.commons.spring.cache

import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * @author sokomishalov
 */
interface CacheService {

    suspend fun <T> get(cacheName: String, key: String, orElse: suspend () -> T): T

    suspend fun evict(cacheName: String, key: String)


    fun <T> getCF(cacheName: String, key: String, orElse: () -> CompletableFuture<T>): CompletableFuture<T> = GlobalScope.future(Unconfined) {
        get(cacheName, key) { orElse().await() }
    }

    fun evictCF(cacheName: String, key: String) = GlobalScope.future(Unconfined) {
        evict(cacheName, key)
    }
}