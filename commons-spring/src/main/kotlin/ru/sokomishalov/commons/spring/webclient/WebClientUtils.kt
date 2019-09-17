package ru.sokomishalov.commons.spring.webclient

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
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
        .clientConnector(ReactorClientHttpConnector(HttpClient
                .create()
                .followRedirect(true)
                .secure { it.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()) }
        ))
        .build()