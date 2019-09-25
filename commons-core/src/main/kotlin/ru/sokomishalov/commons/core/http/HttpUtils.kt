@file:Suppress("unused")

package ru.sokomishalov.commons.core.http

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.tcp.TcpClient

val REACTIVE_NETTY_HTTP_CLIENT: HttpClient = createReactorNettyClient()

fun createReactorNettyClient(
        fixedThreadPoolSize: Int? = null,
        followRedirect: Boolean = true,
        isInsecure: Boolean = true
): HttpClient {
    val httpClient = when (fixedThreadPoolSize) {
        null -> HttpClient.create()
        else -> HttpClient.from(TcpClient.create(ConnectionProvider.fixed("fixed-connection-pool", fixedThreadPoolSize)))
    }

    return httpClient
            .followRedirect(followRedirect)
            .secure {
                when {
                    isInsecure -> it.sslContext(SslContextBuilder
                            .forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .build()
                    )
                }
            }
}