/**
 * Copyright (c) 2019-present Mikhael Sokolov
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
@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.sokomishalov.commons.core.log

import org.slf4j.Logger
import kotlin.reflect.KClass

/**
 * @author sokomishalov
 */

inline fun <reified T : Any> getLogger(): Logger = CustomLoggerFactory.getLogger<T>()

inline fun <T : Any> loggerFor(clazz: Class<T>): Logger = CustomLoggerFactory.getLogger(clazz)

inline fun <T : Any> loggerFor(clazz: KClass<T>): Logger = CustomLoggerFactory.getLogger(clazz.java)

inline fun <T : Any> T.loggerDelegate(): Lazy<Logger> = lazy { CustomLoggerFactory.getLogger(javaClass) }

inline fun <reified T : Any> CustomLoggerFactory.getLogger(): Logger = getLogger(T::class.java)