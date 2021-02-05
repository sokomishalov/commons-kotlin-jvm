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

package ru.sokomishalov.commons.core.scheduled

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.delay as sleep

fun CoroutineScope.runScheduled(
    context: CoroutineContext = EmptyCoroutineContext,
    delay: Duration = Duration.ZERO,
    interval: Duration = Duration.ZERO,
    action: suspend () -> Unit,
): Job = launch(newCoroutineContext(context)) {
    sleep(delay.toMillis())
    if (interval > Duration.ZERO) {
        while (true) {
            action()
            sleep(interval.toMillis())
        }
    } else {
        action()
    }
}