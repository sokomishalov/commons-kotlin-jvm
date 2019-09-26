@file:Suppress("unused")

package ru.sokomishalov.commons.spring.reactive

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.core.codec.ByteArrayDecoder
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import ru.sokomishalov.commons.core.reactor.awaitStrict

/**
 * @author sokomishalov
 */

suspend inline fun <reified T> Publisher<T>.awaitResponse(): ServerResponse =
        ServerResponse
                .ok()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(fromPublisher(this, T::class.java))
                .awaitStrict()

suspend inline fun Mono<FilePart>.convertToByteArray(): ByteArray {
    return this.awaitStrict().convertToByteArray()
}

suspend inline fun FilePart.convertToByteArray(): ByteArray = withContext(IO) {
    ByteArrayDecoder()
            .decodeToMono(content(), ResolvableType.NONE, null, emptyMap<String, Any>())
            .awaitStrict()
}