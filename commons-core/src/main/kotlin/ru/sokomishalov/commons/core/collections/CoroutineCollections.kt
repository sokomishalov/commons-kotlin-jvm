@file:Suppress("unused")

package ru.sokomishalov.commons.core.collections

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.sokomishalov.commons.core.common.unit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * @author sokomishalov
 */

suspend inline fun <T> Iterable<T>.aForEach(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        noinline block: suspend CoroutineScope.(T) -> Unit
) = withContext(context) {
    map { async { block(it) } }.awaitAll().unit()
}

suspend inline fun <T, R> Iterable<T>.aMap(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        noinline transform: suspend CoroutineScope.(T) -> R
): List<R> = withContext(context) {
    map { async { transform(it) } }.awaitAll()
}

suspend inline fun <T, R> Iterable<T>.aFlatMap(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        crossinline transform: suspend CoroutineScope.(T) -> Iterable<R>
): List<R> = withContext(context) {
    val destination = mutableListOf<R>()
    map { async { destination.addAll(transform(it)) } }.awaitAll()
    destination
}

suspend inline fun <T> Iterable<T>.aFilter(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        crossinline predicate: suspend CoroutineScope.(T) -> Boolean
): List<T> = withContext(context) {
    val destination = mutableListOf<T>()
    map { async { if (predicate(it)) destination.add(it) } }.awaitAll()
    destination
}