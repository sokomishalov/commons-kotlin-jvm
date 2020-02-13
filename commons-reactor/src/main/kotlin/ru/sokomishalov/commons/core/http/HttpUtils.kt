/**
 * Copyright 2019-2020 the original author or authors.
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

import io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.timeout.ReadTimeoutHandler
import reactor.netty.channel.BootstrapHandlers
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.tcp.TcpClient
import ru.sokomishalov.commons.core.string.isNotNullOrBlank
import java.time.Duration

val REACTIVE_NETTY_HTTP_CLIENT: HttpClient = createReactorNettyClient()

fun createReactorNettyClient(
        baseUrl: String? = null,
        readTimeout: Duration? = null,
        connectionTimeout: Duration? = null,
        fixedThreadPoolSize: Int? = null,
        followRedirect: Boolean = true,
        isInsecure: Boolean = true,
        loggingHandler: LoggingHandler? = null
): HttpClient {
    return HttpClient
            .from(
                    if (fixedThreadPoolSize == null) TcpClient.create()
                    else TcpClient.create(ConnectionProvider.fixed("fixed-connection-pool", fixedThreadPoolSize))
            )
            .followRedirect(followRedirect)
            .run {
                if (baseUrl.isNotNullOrBlank()) baseUrl(baseUrl)
                else this
            }
            .run {
                if (isInsecure) secure { ssl ->
                    ssl.sslContext(SslContextBuilder
                            .forClient()
                            .trustManager(InsecureTrustManagerFactory.INSTANCE)
                            .build()
                    )
                }
                else this
            }
            .run {
                tcpConfiguration { client ->
                    client
                            .run {
                                if (loggingHandler != null) bootstrap { b -> BootstrapHandlers.updateLogSupport(b, loggingHandler) } else this
                            }
                            .run {
                                if (connectionTimeout != null) option(CONNECT_TIMEOUT_MILLIS, connectionTimeout.toMillis().toInt()) else this
                            }
                            .run {
                                if (readTimeout != null) doOnConnected { it.addHandlerLast(ReadTimeoutHandler(readTimeout.seconds.toInt())) } else this
                            }
                }

            }
}