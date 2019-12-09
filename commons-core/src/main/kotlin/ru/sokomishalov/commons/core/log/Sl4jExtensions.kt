package ru.sokomishalov.commons.core.log

import org.slf4j.Logger

/**
 * @author sokomishalov
 */

fun Logger.info(lazyMessage: () -> String) = info(lazyMessage())

fun Logger.debug(lazyMessage: () -> String) = debug(lazyMessage())

fun Logger.warn(lazyMessage: () -> String) = warn(lazyMessage())

fun Logger.trace(lazyMessage: () -> String) = trace(lazyMessage())

fun Logger.error(throwable: Throwable, lazyMessage: () -> String) = error(lazyMessage(), throwable)