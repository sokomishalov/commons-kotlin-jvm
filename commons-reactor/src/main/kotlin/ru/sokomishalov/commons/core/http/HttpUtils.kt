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
import ru.sokomishalov.commons.core.string.isNotNullOrBlank

val REACTIVE_NETTY_HTTP_CLIENT: HttpClient = createReactorNettyClient()

fun createReactorNettyClient(
        baseUrl: String? = null,
        fixedThreadPoolSize: Int? = null,
        followRedirect: Boolean = true,
        isInsecure: Boolean = true,
        loggingHandler: LoggingHandler? = null
): HttpClient {
    return HttpClient
            .from(
                    when (fixedThreadPoolSize) {
                        null -> TcpClient.create()
                        else -> TcpClient.create(ConnectionProvider.fixed("fixed-connection-pool", fixedThreadPoolSize))
                    }
            )
            .run {
                when {
                    baseUrl.isNotNullOrBlank() -> this.baseUrl(baseUrl)
                    else -> this
                }
            }
            .run {
                when {
                    isInsecure -> this.secure { ssl ->
                        ssl.sslContext(SslContextBuilder
                                .forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                                .build()
                        )
                    }
                    else -> this
                }
            }
            .run {
                when {
                    loggingHandler != null -> this.tcpConfiguration { tc ->
                        tc.bootstrap { b ->
                            BootstrapHandlers.updateLogSupport(b, loggingHandler)
                        }
                    }
                    else -> this
                }
            }
            .followRedirect(followRedirect)
}