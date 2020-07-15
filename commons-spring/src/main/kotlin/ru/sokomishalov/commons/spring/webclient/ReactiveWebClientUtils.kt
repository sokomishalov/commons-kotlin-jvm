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

package ru.sokomishalov.commons.spring.webclient

import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.CodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import ru.sokomishalov.commons.core.http.createReactorNettyClient
import ru.sokomishalov.commons.core.string.isNotNullOrBlank
import ru.sokomishalov.commons.spring.serialization.JACKSON_DECODER
import ru.sokomishalov.commons.spring.serialization.JACKSON_ENCODER

/**
 * @author sokomishalov
 */


val REACTIVE_WEB_CLIENT: WebClient = createReactiveWebClient()

fun createReactiveWebClient(
        baseUrl: String? = null,
        reactorNettyClient: HttpClient = createReactorNettyClient(baseUrl = baseUrl),
        clientConnector: ClientHttpConnector = ReactorClientHttpConnector(reactorNettyClient),
        filters: List<ExchangeFilterFunction> = emptyList(),
        encoder: Jackson2JsonEncoder = JACKSON_ENCODER,
        decoder: Jackson2JsonDecoder = JACKSON_DECODER,
        maxBufferSize: Int = -1,
        codecs: CodecConfigurer.DefaultCodecs.() -> Unit = {
            jackson2JsonEncoder(encoder)
            jackson2JsonDecoder(decoder)
            maxInMemorySize(maxBufferSize)
        }
): WebClient {
    return WebClient
            .builder()
            .run {
                when {
                    baseUrl.isNotNullOrBlank() -> baseUrl(baseUrl)
                    else -> this
                }
            }
            .clientConnector(clientConnector)
            .exchangeStrategies(ExchangeStrategies
                    .builder()
                    .codecs { ccc -> ccc.defaultCodecs().apply(codecs) }
                    .build()
            )
            .apply {
                filters.forEach { f -> it.filter(f) }
            }
            .build()
}