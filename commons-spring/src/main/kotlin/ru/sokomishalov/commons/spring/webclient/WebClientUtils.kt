@file:Suppress("unused")

package ru.sokomishalov.commons.spring.webclient

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import ru.sokomishalov.commons.core.http.REACTIVE_NETTY_HTTP_CLIENT
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER

/**
 * @author sokomishalov
 */

val REACTIVE_WEB_CLIENT: WebClient = WebClient
        .builder()
        .exchangeStrategies(ExchangeStrategies
                .builder()
                .codecs {
                    it.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(OBJECT_MAPPER))
                    it.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(OBJECT_MAPPER))
                }
                .build()
        )
        .clientConnector(ReactorClientHttpConnector(REACTIVE_NETTY_HTTP_CLIENT))
        .build()