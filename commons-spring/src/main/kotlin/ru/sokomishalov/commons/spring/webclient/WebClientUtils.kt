@file:Suppress("unused")

package ru.sokomishalov.commons.spring.webclient

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import ru.sokomishalov.commons.core.http.REACTIVE_NETTY_HTTP_CLIENT
import ru.sokomishalov.commons.spring.serialization.JACKSON_DECODER
import ru.sokomishalov.commons.spring.serialization.JACKSON_ENCODER

/**
 * @author sokomishalov
 */

val REACTIVE_WEB_CLIENT: WebClient = WebClient
        .builder()
        .exchangeStrategies(ExchangeStrategies
                .builder()
                .codecs {
                    it.defaultCodecs().apply {
                        jackson2JsonEncoder(JACKSON_ENCODER)
                        jackson2JsonDecoder(JACKSON_DECODER)
                    }
                }
                .build()
        )
        .clientConnector(ReactorClientHttpConnector(REACTIVE_NETTY_HTTP_CLIENT))
        .build()