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

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.sokomishalov.commons.core.common.unit


/**
 * @author sokomishalov
 */


suspend inline fun <reified T> Flux<T>.await(): List<T> = collectList().await() ?: emptyList()

suspend inline fun <reified T> Mono<T>.await(): T? = awaitFirstOrNull()

suspend inline fun <reified T> Flux<T>.awaitUnit(): Unit = await().unit()

suspend inline fun <reified T> Mono<T>.awaitUnit(): Unit = await().unit()

suspend inline fun <reified T> Mono<T>.awaitOrElse(defaultValue: () -> T): T = awaitFirstOrNull() ?: defaultValue()

suspend inline fun <reified T> Mono<T>.awaitStrict(): T = awaitFirst()