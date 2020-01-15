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
@file:Suppress("unused", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package ru.sokomishalov.commons.spring.webclient

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
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
        encoder: Jackson2JsonEncoder = JACKSON_ENCODER,
        decoder: Jackson2JsonDecoder = JACKSON_DECODER,
        fixedThreadPoolSize: Int? = null,
        maxBufferSize: Int? = null,
        filters: List<ExchangeFilterFunction> = emptyList()
): WebClient {
    return WebClient
            .builder()
            .run {
                when {
                    baseUrl.isNotNullOrBlank() -> this.baseUrl(baseUrl)
                    else -> this
                }
            }
            .clientConnector(ReactorClientHttpConnector(createReactorNettyClient(baseUrl = baseUrl, fixedThreadPoolSize = fixedThreadPoolSize)))
            .exchangeStrategies(ExchangeStrategies
                    .builder()
                    .codecs { ccc ->
                        ccc.defaultCodecs().apply {
                            jackson2JsonEncoder(encoder)
                            jackson2JsonDecoder(decoder)
                            maxBufferSize?.let { maxInMemorySize(it) }
                        }
                    }
                    .build()
            )
            .apply {
                filters.forEach { f -> it.filter(f) }
            }
            .build()
}