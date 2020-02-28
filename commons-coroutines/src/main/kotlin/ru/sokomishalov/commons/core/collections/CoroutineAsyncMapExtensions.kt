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

package ru.sokomishalov.commons.core.collections

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.sokomishalov.commons.core.common.unit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * @author sokomishalov
 */

suspend inline fun <K, V> Map<out K, V>.aForEach(
        scope: CoroutineScope? = null,
        context: CoroutineContext = scope?.coroutineContext ?: EmptyCoroutineContext,
        crossinline block: suspend CoroutineScope.(Map.Entry<K, V>) -> Unit
): Unit = withContext(scope.plusContext(context)) {
    map { async { block(it) } }.awaitAll().unit()
}