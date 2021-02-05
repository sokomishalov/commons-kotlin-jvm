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
@file:Suppress("unused")

package ru.sokomishalov.commons.core.time

import java.lang.System.currentTimeMillis
import java.time.Duration
import java.time.Duration.ofMillis

/**
 * @author sokomishalov
 */

inline fun <R> runAndMeasure(block: () -> R): Pair<R, Duration> {
    val start = currentTimeMillis()
    val result = block()
    return result to ofMillis(currentTimeMillis() - start)
}

inline fun <T> runAndLogExecutionTime(
    logAction: (Duration) -> Unit = { println("Execution time: ${it.humanReadable()}") },
    block: () -> T
): T {
    val pair = runAndMeasure { block() }
    logAction(pair.second)
    return pair.first
}