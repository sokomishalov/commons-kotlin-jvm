/**
 * Copyright 2019-2019 the original author or authors.
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
@file:Suppress("JAVA_CLASS_ON_COMPANION", "unused")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger

/**
 * @author sokomishalov
 */
interface Loggable {

    val logger: Logger get() = CustomLoggerFactory.getLogger(javaClass)

    fun log(s: String?) = logger.info(s)

    fun log(lazyMessage: () -> String?) = logger.info(lazyMessage)

    fun logInfo(s: String?) = logger.info(s)

    fun logInfo(lazyMessage: () -> String?) = logger.info(lazyMessage())

    fun logDebug(s: String?) = logger.debug(s)

    fun logDebug(lazyMessage: () -> String?) = logger.debug(lazyMessage)

    fun logWarn(t: Throwable) = logWarn(t.message)

    fun logWarn(message: String?) = logger.warn(message)

    fun logWarn(lazyMessage: () -> String?) = logger.warn(lazyMessage)

    fun logTrace(message: String?) = logger.trace(message)

    fun logTrace(lazyMessage: () -> String?) = logger.trace(lazyMessage)

    fun logError(t: Throwable) = logError(t.message, t)

    fun logError(message: String?, t: Throwable) = logger.error(message, t)

    fun logError(throwable: Throwable, lazyMessage: () -> String? = { throwable.message }) = logger.error(throwable, lazyMessage)
}