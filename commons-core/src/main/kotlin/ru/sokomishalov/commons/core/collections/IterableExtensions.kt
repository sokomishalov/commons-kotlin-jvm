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

package ru.sokomishalov.commons.core.collections

/**
 * @author sokomishalov
 */

inline fun <K, V> Iterable<Map<K, V>>.toMap(): Map<K, V> = fold(mutableMapOf(), { acc, map -> acc.putAll(map).let { acc } })

inline fun <T, K, V> Iterable<T>.toMap(transform: (T) -> Pair<K, V>): Map<K, V> = map(transform).toMap()