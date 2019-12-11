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
        noinline transform: suspend CoroutineScope.(T) -> Iterable<R>
): List<R> = withContext(context) {
    val destination = mutableListOf<R>()
    map { async { destination.addAll(transform(it)) } }.awaitAll()
    destination
}

suspend inline fun <T> Iterable<T>.aFilter(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        noinline predicate: suspend CoroutineScope.(T) -> Boolean
): List<T> = withContext(context) {
    val destination = mutableListOf<T>()
    map { async { if (predicate(it)) destination.add(it) } }.awaitAll()
    destination
}