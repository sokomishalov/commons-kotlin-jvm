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
import org.slf4j.Marker

/**
 * @author sokomishalov
 */

inline fun Logger.info(marker: Marker? = null, lazyMessage: () -> String?) {
    if (isInfoEnabled) {
        if (marker != null) info(marker, lazyMessage()) else info(lazyMessage())
    }
}

inline fun Logger.debug(marker: Marker? = null, lazyMessage: () -> String?) {
    if (isDebugEnabled) {
        if (marker != null) debug(marker, lazyMessage()) else debug(lazyMessage())
    }
}

inline fun Logger.warn(marker: Marker? = null, lazyMessage: () -> String?) {
    if (isWarnEnabled) {
        if (marker != null) warn(marker, lazyMessage()) else warn(lazyMessage())
    }
}

inline fun Logger.trace(marker: Marker? = null, lazyMessage: () -> String?) {
    if (isTraceEnabled) {
        if (marker != null) trace(marker, lazyMessage()) else trace(lazyMessage())
    }
}

inline fun Logger.error(marker: Marker? = null, throwable: Throwable, lazyMessage: () -> String? = { throwable.message }) {
    if (isErrorEnabled) {
        if (marker != null) error(marker, lazyMessage(), throwable) else error(lazyMessage(), throwable)
    }
}