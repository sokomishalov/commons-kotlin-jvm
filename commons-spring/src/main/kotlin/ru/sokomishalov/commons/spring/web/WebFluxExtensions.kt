/**
 * Copyright (c) 2019-present Mikhael Sokolov
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

package ru.sokomishalov.commons.spring.web

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