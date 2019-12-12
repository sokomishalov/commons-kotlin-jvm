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

inline fun <reified T : Any> T.loggerDelegate(): Lazy<Logger> = lazy { loggerFor(javaClass.unwrapCompanionClass()) }

