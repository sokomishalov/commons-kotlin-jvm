@file:Suppress("unused")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

/**
 * @author sokomishalov
 */

fun <T> loggerFor(clazz: Class<T>): Logger = getLogger(clazz)

fun <T : Loggable> T.logger(): Logger = loggerFor(javaClass)