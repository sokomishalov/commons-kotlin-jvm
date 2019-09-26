@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package ru.sokomishalov.commons.core.reactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.flux
import kotlinx.coroutines.reactor.mono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.sokomishalov.commons.core.common.unit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import reactor.core.publisher.Mono.empty as monoEmpty


/**
 * @author sokomishalov
 */

fun <T> aFlux(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend () -> Iterable<T>
): Flux<T> {
    return flux(context) { block().forEach { send(it) } }
}

fun <T> aMono(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T?
): Mono<T> {
    return mono(context, block)
}

suspend inline fun <reified T> Flux<T>?.await(): List<T> = this?.collectList().await() ?: emptyList()

suspend inline fun <reified T> Mono<T>?.await(): T? = this?.awaitFirstOrNull()

suspend inline fun <reified T> Flux<T>?.awaitUnit(): Unit = this.await().unit()

suspend inline fun <reified T> Mono<T>?.awaitUnit(): Unit = this.await().unit()

suspend inline fun <reified T> Mono<T>.awaitOrElse(defaultValue: () -> T): T = awaitFirstOrNull() ?: defaultValue()

suspend inline fun <reified T> Mono<T>?.awaitStrict(): T = this?.awaitFirstOrNull() ?: monoEmpty<T>().awaitSingle()