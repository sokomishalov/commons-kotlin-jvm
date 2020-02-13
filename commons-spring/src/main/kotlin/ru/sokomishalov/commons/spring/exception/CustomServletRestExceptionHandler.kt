@file:Suppress("RedundantModalityModifier", "unused")

package ru.sokomishalov.commons.spring.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.netty.handler.timeout.TimeoutException
import org.springframework.core.codec.DecodingException
import org.springframework.core.codec.EncodingException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.GATEWAY_TIMEOUT
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import java.io.PrintWriter
import java.io.StringWriter
import java.net.ConnectException
import java.time.format.DateTimeParseException
import java.util.*
import javax.naming.AuthenticationException
import javax.naming.NoPermissionException
import javax.naming.OperationNotSupportedException
import javax.servlet.http.HttpServletRequest
import kotlin.NoSuchElementException


@ControllerAdvice
open class CustomServletRestExceptionHandler @JvmOverloads constructor(
        private val includeStacktrace: Boolean = true
) {

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    open fun badRequestException(e: Exception, exchange: HttpServletRequest): ResponseEntity<*> = exchange.toErrorResponseEntity(HttpStatus.BAD_REQUEST, e)

    @ExceptionHandler(
            AccessDeniedException::class,
            OperationNotSupportedException::class,
            NoPermissionException::class
    )
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    open fun forbiddenException(e: Exception, exchange: HttpServletRequest): ResponseEntity<*> = exchange.toErrorResponseEntity(HttpStatus.FORBIDDEN, e)

    @ExceptionHandler(
            AuthenticationException::class
    )
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    open fun unauthorizedException(e: Exception, exchange: HttpServletRequest): ResponseEntity<*> = exchange.toErrorResponseEntity(HttpStatus.UNAUTHORIZED, e)


    @ExceptionHandler(
            UnsupportedOperationException::class,
            NotImplementedError::class
    )
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ResponseBody
    open fun handleNotRealized(e: Exception, exchange: HttpServletRequest): ResponseEntity<*> = exchange.toErrorResponseEntity(HttpStatus.NOT_IMPLEMENTED, e)

    @ExceptionHandler(
            ConnectException::class,
            TimeoutException::class
    )
    @ResponseStatus(GATEWAY_TIMEOUT)
    @ResponseBody
    open fun gatewayTimeoutException(e: Exception, exchange: HttpServletRequest): ResponseEntity<*> = exchange.toErrorResponseEntity(status = GATEWAY_TIMEOUT, e = e)


    open fun HttpServletRequest.toErrorResponseEntity(status: HttpStatus, e: Exception): ResponseEntity<*> {
        val map = mutableMapOf(
                "timestamp" to Date(),
                "path" to requestURI,
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