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
package ru.sokomishalov.commons.core.log

import org.slf4j.Logger

/**
 * @author sokomishalov
 */

inline fun Logger.info(lazyMessage: () -> String?) = if (isInfoEnabled) info(lazyMessage()) else Unit

inline fun Logger.debug(lazyMessage: () -> String?) = if (isDebugEnabled) debug(lazyMessage()) else Unit

inline fun Logger.warn(lazyMessage: () -> String?) = if (isWarnEnabled) warn(lazyMessage()) else Unit

inline fun Logger.trace(lazyMessage: () -> String?) = if (isTraceEnabled) trace(lazyMessage()) else Unit

inline fun Logger.error(throwable: Throwable, lazyMessage: () -> String? = { throwable.message }) = if (isErrorEnabled) error(lazyMessage(), throwable) else Unit