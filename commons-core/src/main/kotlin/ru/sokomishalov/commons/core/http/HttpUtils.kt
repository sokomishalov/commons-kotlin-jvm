@file:Suppress("unused")

package ru.sokomishalov.commons.core.http

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import reactor.netty.http.client.HttpClient

val REACTIVE_NETTY_HTTP_CLIENT: HttpClient = HttpClient
        .create()
        .followRedirect(true)
        .secure {
            it.sslContext(SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build()
            )
        }