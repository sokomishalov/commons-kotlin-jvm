package ru.sokomishalov.commons.spring.exception

import java.io.PrintWriter
import java.util.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

internal object NullHttpServletServletHttpResponse : HttpServletResponse {
    override fun encodeURL(url: String?): String = noop()
    override fun encodeUrl(url: String?): String = noop()
    override fun addIntHeader(name: String?, value: Int): Unit = noop()
    override fun addCookie(cookie: Cookie?): Unit = noop()
    override fun encodeRedirectUrl(url: String?): String = noop()
    override fun flushBuffer(): Unit = noop()
    override fun encodeRedirectURL(url: String?): String = noop()
    override fun sendRedirect(location: String?): Unit = noop()
    override fun setBufferSize(size: Int): Unit = noop()
    override fun getLocale(): Locale = noop()
    override fun sendError(sc: Int, msg: String?): Unit = noop()
    override fun sendError(sc: Int): Unit = noop()
    override fun setContentLengthLong(length: Long): Unit = noop()
    override fun setCharacterEncoding(charset: String?): Unit = noop()
    override fun addDateHeader(name: String?, date: Long): Unit = noop()
    override fun setLocale(loc: Locale?): Unit = noop()
    override fun getHeaders(name: String?): MutableCollection<String> = noop()
    override fun addHeader(name: String?, value: String?): Unit = noop()
    override fun setContentLength(len: Int): Unit = noop()
    override fun getBufferSize(): Int = noop()
    override fun resetBuffer(): Unit = noop()
    override fun reset(): Unit = noop()
    override fun setDateHeader(name: String?, date: Long): Unit = noop()
    override fun getStatus(): Int = noop()
    override fun getCharacterEncoding(): String = noop()
    override fun isCommitted(): Boolean = noop()
    override fun setStatus(sc: Int): Unit = noop()
    override fun setStatus(sc: Int, sm: String?): Unit = noop()
    override fun getHeader(name: String?): String = noop()
    override fun getContentType(): String = noop()
    override fun getWriter(): PrintWriter = noop()
    override fun containsHeader(name: String?): Boolean = noop()
    override fun setIntHeader(name: String?, value: Int): Unit = noop()
    override fun getHeaderNames(): MutableCollection<String> = noop()
    override fun setHeader(name: String?, value: String?): Unit = noop()
    override fun getOutputStream(): ServletOutputStream = noop()
    override fun setContentType(type: String?): Unit = noop()

    private fun noop(): Nothing = throw NotImplementedError()
}