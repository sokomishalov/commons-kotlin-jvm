@file:Suppress("JAVA_CLASS_ON_COMPANION", "unused")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger

/**
 * @author sokomishalov
 */
interface Loggable {

    val log: Logger get() = CustomLoggerFactory.getLogger(javaClass)

    fun log(s: String?) = log.info(s)

    fun logInfo(s: String?) = log.info(s)

    fun logDebug(s: String?) = log.debug(s)

    fun logWarn(t: Throwable) = logWarn(t.message)

    fun logWarn(message: String?) = log.warn(message)

    fun logError(t: Throwable) = logError(t.message, t)

    fun logError(message: String?, t: Throwable) = log.error(message, t)
}