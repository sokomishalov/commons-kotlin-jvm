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
@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.sokomishalov.commons.core.numbers

/**
 * @author sokomishalov
 */

@PublishedApi
internal const val MULTIPLIER: Int = 1024

// kilobyte
inline val Int.kb: Long get() = this.toLong() * MULTIPLIER
inline val Long.kb: Long get() = this * MULTIPLIER

// megabyte
inline val Int.mb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER
inline val Long.mb: Long get() = this * MULTIPLIER * MULTIPLIER

// gigabyte
inline val Int.gb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER * MULTIPLIER
inline val Long.gb: Long get() = this * MULTIPLIER * MULTIPLIER * MULTIPLIER

// terabyte
inline val Int.tb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER * MULTIPLIER * MULTIPLIER
inline val Long.tb: Long get() = this * MULTIPLIER * MULTIPLIER * MULTIPLIER * MULTIPLIER