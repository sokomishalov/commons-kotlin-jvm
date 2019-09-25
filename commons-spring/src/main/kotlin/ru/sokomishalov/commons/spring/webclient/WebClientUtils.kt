@file:Suppress("unused", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package ru.sokomishalov.commons.spring.webclient

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import ru.sokomishalov.commons.core.http.createReactorNettyClient
import ru.sokomishalov.commons.spring.serialization.JACKSON_DECODER
import ru.sokomishalov.commons.spring.serialization.JACKSON_ENCODER

/**
 * @author sokomishalov
 */

val REACTIVE_WEB_CLIENT: WebClient = createReactiveWebClient()

fun createReactiveWebClient(
        fixedThreadPoolSize: Int? = null,
        encoder: Jackson2JsonEncoder = JACKSON_ENCODER,
        decoder: Jackson2JsonDecoder = JACKSON_DECODER
): WebClient {
    return WebClient
            .builder()
            .exchangeStrategies(ExchangeStrategies
                    .builder()
                    .codecs {
                        it.defaultCodecs().apply {
                            jackson2JsonEncoder(encoder)
                            jackson2JsonDecoder(decoder)
                        }
                    }
                    .build()
            )
            .clientConnector(ReactorClientHttpConnector(createReactorNettyClient(fixedThreadPoolSize)))
            .build()
}