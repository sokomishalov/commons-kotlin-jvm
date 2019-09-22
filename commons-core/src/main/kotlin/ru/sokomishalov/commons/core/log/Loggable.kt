@file:Suppress("JAVA_CLASS_ON_COMPANION", "unused")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger

/**
 * @author sokomishalov
 */
interface Loggable {

    val logger: Logger get() = CustomLoggerFactory.getLogger(javaClass)

    fun log(s: String?) = logger.info(s)

    fun logInfo(s: String?) = logger.info(s)

    fun logDebug(s: String?) = logger.debug(s)

    fun logWarn(t: Throwable) = logWarn(t.message)

    fun logWarn(message: String?) = logger.warn(message)

    fun logError(t: Throwable) = logError(t.message, t)

    fun logError(message: String?, t: Throwable) = logger.error(message, t)
}