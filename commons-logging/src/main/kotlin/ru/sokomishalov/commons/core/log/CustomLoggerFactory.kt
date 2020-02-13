/**
 * Copyright 2019-2020 the original author or authors.
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
package ru.sokomishalov.commons.core.log

import org.slf4j.Logger
import ru.sokomishalov.commons.core.reflection.unwrapCompanionClass
import java.util.concurrent.ConcurrentHashMap

/**
 * @author sokomishalov
 */
object CustomLoggerFactory {

    private val loggersMap: MutableMap<String, Logger> = ConcurrentHashMap()

    fun <T : Loggable> getLogger(clazz: Class<T>): Logger {
        val nonCompanionClazz = clazz.unwrapCompanionClass()
        return getLogger(nonCompanionClazz.name)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getLogger(className: String): Logger {
        val logger = loggersMap[className]
        return when {
            logger != null -> logger
            else -> {
                val newLogger = loggerFor(className)
                loggersMap[className] = newLogger
                newLogger
            }
        }
    }
}