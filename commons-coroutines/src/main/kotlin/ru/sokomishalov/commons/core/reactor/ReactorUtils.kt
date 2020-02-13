/**
 * Copyright 2019-2020 the original author or authors.
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
@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package ru.sokomishalov.commons.core.reactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.reactor.flux
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * @author sokomishalov
 */

fun <T> aFlux(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Iterable<T>
): Flux<T> {
    return flux(context) { block().forEach { send(it) } }
}

fun <T> aMono(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T?
): Mono<T> {
    return mono(context, block)
}
