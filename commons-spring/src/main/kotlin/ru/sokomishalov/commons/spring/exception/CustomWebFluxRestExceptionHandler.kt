/**
 * Copyright (c) 2019-present Mikhael Sokolov
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
@file:Suppress(
        "unused",
        "MemberVisibilityCanBePrivate"
)

package ru.sokomishalov.commons.spring.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.netty.handler.timeout.TimeoutException
import org.springframework.core.codec.DecodingException
import org.springframework.core.codec.EncodingException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import ru.sokomishalov.commons.core.log.Loggable
import java.io.PrintWriter
import java.io.StringWriter
import java.net.ConnectException
import java.time.format.DateTimeParseException
import java.util.*
import javax.naming.AuthenticationException
import javax.naming.NoPermissionException
import javax.naming.OperationNotSupportedException
import kotlin.NoSuchElementException

open class CustomWebFluxRestExceptionHandler @JvmOverloads constructor(
        private val includeStacktrace: Boolean = true
) {

    companion object : Loggable

    @ExceptionHandler(
            IllegalArgumentException::class,
            NoSuchElementException::class,
            InvalidFormatException::class,
            DateTimeParseException::class,
            HttpMessageNotReadableException::class,
            MethodArgumentNotValidException::class,
            WebExchangeBindException::class,
            DecodingException::class,
            EncodingException::class
    )
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    open fun badRequestException(e: Exception, exchange: ServerWebExchange): ResponseEntity<*> = exchange.toErrorResponseEntity(BAD_REQUEST, e)

    @ExceptionHandler(
            AccessDeniedException::class,
            OperationNotSupportedException::class,
            NoPermissionException::class
    )
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    open fun forbiddenException(e: Exception, exchange: ServerWebExchange): ResponseEntity<*> = exchange.toErrorResponseEntity(FORBIDDEN, e)

    @ExceptionHandler(
            AuthenticationException::class
    )
    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    open fun unauthorizedException(e: Exception, exchange: ServerWebExchange): ResponseEntity<*> = exchange.toErrorResponseEntity(UNAUTHORIZED, e)


    @ExceptionHandler(
            UnsupportedOperationException::class,
            NotImplementedError::class
    )
    @ResponseStatus(NOT_IMPLEMENTED)
    @ResponseBody
    open fun handleNotRealized(e: Exception, exchange: ServerWebExchange): ResponseEntity<*> = exchange.toErrorResponseEntity(NOT_IMPLEMENTED, e)

    @ExceptionHandler(
            ConnectException::class,
            TimeoutException::class
    )
    @ResponseStatus(GATEWAY_TIMEOUT)
    @ResponseBody
    open fun timeoutException(e: Exception, exchange: ServerWebExchange): ResponseEntity<*> = exchange.toErrorResponseEntity(GATEWAY_TIMEOUT, e)


    open fun ServerWebExchange.toErrorResponseEntity(status: HttpStatus, e: Exception): ResponseEntity<*> {
        when {
            status.is4xxClientError -> logWarn(e)
            status.is5xxServerError -> logError(e)
        }

        val map = mutableMapOf(
                "timestamp" to Date(),
                "path" to request.path,
                "status" to status.value(),
                "error" to e.javaClass,
                "message" to (e.message ?: status.reasonPhrase)
        )

        if (includeStacktrace) {
            val stackTrace = StringWriter()
            e.printStackTrace(PrintWriter(stackTrace))
            stackTrace.flush()
            map["trace"] = stackTrace.toString()
        }

        return ResponseEntity.status(status).body(map)
    }
}