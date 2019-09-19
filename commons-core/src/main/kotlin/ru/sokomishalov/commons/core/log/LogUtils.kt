@file:Suppress("unused")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import ru.sokomishalov.commons.core.reflection.unwrapCompanionClass

/**
 * @author sokomishalov
 */

fun <T> loggerFor(clazz: Class<T>): Logger = getLogger(clazz)

fun loggerFor(name: String): Logger = getLogger(name)

inline fun <reified T : Loggable> T.logger(): Logger = loggerFor(javaClass)

inline fun <reified T : Any> T.loggerDelegate(): Lazy<Logger> = lazy { loggerFor(unwrapCompanionClass(javaClass)) }

