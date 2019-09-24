@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ru.sokomishalov.commons.spring.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.core.codec.DecodingException
import org.springframework.core.codec.EncodingException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.status
import org.springframework.http.codec.DecoderHttpMessageReader
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.server.ServerRequest.create
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.spring.serialization.JACKSON_DECODER
import java.time.format.DateTimeParseException
import javax.naming.AuthenticationException
import javax.naming.NoPermissionException
import javax.naming.OperationNotSupportedException
import ru.sokomishalov.commons.spring.const.INTERNAL_SERVER_ERROR as ISE

open class CustomExceptionHandler : Loggable {

    companion object {
        private val messageReaders: List<HttpMessageReader<*>> = listOf(DecoderHttpMessageReader(JACKSON_DECODER))
    }

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


    fun ServerWebExchange.toErrorResponseEntity(status: HttpStatus, e: Exception): ResponseEntity<*> {
        when {
            status.is5xxServerError -> logError(ISE, e)
            status.is4xxClientError -> logWarn(e)
        }

        val defaultAttributes = DefaultErrorAttributes().also {
            it.storeErrorInformation(ResponseStatusException(status, e.message), this)
        }

        val attrMap = defaultAttributes.getErrorAttributes(create(this, messageReaders), false)

        return status(status).body(attrMap)
    }
}