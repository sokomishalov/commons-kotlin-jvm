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
@file:Suppress("unused")

package ru.sokomishalov.commons.core.time

import java.time.Duration
import java.time.Period
import java.time.temporal.TemporalAmount

/**
 * @author sokomishalov
 */

fun Duration.humanReadable(): String? {
    return toString()
            .substring(2)
            .replace("(\\d[HMS])(?!$)".toRegex(), "$1 ")
            .toLowerCase()
}

inline val TemporalAmount.ms: Long
    get() = when (this) {
        is Duration -> this.toMillis()
        is Period -> this.days.toLong() * 24 * 60 * 60 * 1000
        else -> Duration.from(this).toMillis()
    }