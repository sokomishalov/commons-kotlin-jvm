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

package ru.sokomishalov.commons.core.string

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * @author sokomishalov
 */

@UseExperimental(ExperimentalContracts::class)
inline fun CharSequence?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }
    return (this == null || this.isBlank()).not()
}

// https://stackoverflow.com/a/1248627/5843129
fun String.convertGlobToRegex(): Regex {
    val sb = StringBuilder(length)
    var inGroup = 0
    var inClass = 0
    var firstIndexInClass = -1
    val arr = toCharArray()
    var i = 0
    while (i < arr.size) {
        when (val ch = arr[i]) {
            '\\' -> if (++i >= arr.size) {
                sb.append('\\')
            } else {
                val next = arr[i]
                when (next) {
                    ',' -> {
                    }
                    'Q', 'E' -> {
                        // extra escape needed
                        sb.append('\\')
                        sb.append('\\')
                    }
                    else -> sb.append('\\')
                }// escape not needed
                sb.append(next)
            }
            '*' -> if (inClass == 0)
                sb.append(".*")
            else
                sb.append('*')
            '?' -> if (inClass == 0)
                sb.append('.')
            else
                sb.append('?')
            '[' -> {
                inClass++
                firstIndexInClass = i + 1
                sb.append('[')
            }
            ']' -> {
                inClass--
                sb.append(']')
            }
            '.', '(', ')', '+', '|', '^', '$', '@', '%' -> {
                if (inClass == 0 || firstIndexInClass == i && ch == '^')
                    sb.append('\\')
                sb.append(ch)
            }
            '!' -> if (firstIndexInClass == i)
                sb.append('^')
            else
                sb.append('!')
            '{' -> {
                inGroup++
                sb.append('(')
            }
            '}' -> {
                inGroup--
                sb.append(')')
            }
            ',' -> if (inGroup > 0)
                sb.append('|')
            else
                sb.append(',')
            else -> sb.append(ch)
        }
        i++
    }
    return sb.toString().toRegex()
}