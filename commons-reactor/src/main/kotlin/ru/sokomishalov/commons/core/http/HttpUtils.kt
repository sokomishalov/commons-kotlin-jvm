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
@file:Suppress("unused")

package ru.sokomishalov.commons.core.http

import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import reactor.netty.channel.BootstrapHandlers
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.tcp.TcpClient

val REACTIVE_NETTY_HTTP_CLIENT: HttpClient = createReactorNettyClient()

fun createReactorNettyClient(
        fixedThreadPoolSize: Int? = null,
        followRedirect: Boolean = true,
        isInsecure: Boolean = true,
        loggingHandler: LoggingHandler? = null
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
            .tcpConfiguration { tc ->
                tc.bootstrap {
                    when {
                        loggingHandler != null -> BootstrapHandlers.updateLogSupport(it, loggingHandler)
                        else -> it
                    }
                }
            }
}