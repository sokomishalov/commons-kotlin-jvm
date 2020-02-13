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

package ru.sokomishalov.commons.core.random

/**
 * @author sokomishalov
 */

private val LOWER_CASE_ALPHABET: CharRange = ('a'..'z')
private val UPPER_CASE_ALPHABET: CharRange = ('A'..'Z')
private val NUMBERS: CharRange = ('0'..'9')

fun randomString(
        length: Int = 20,
        useLetters: Boolean = true,
        useDigits: Boolean = true,
        lowerCase: Boolean = true,
        upperCase: Boolean = true
): String {
    require(useLetters || useDigits)
    require(!useLetters || lowerCase || upperCase)

    val fullAlphabet: MutableList<Char> = mutableListOf()

    if (useDigits) fullAlphabet += NUMBERS
    if (useLetters && lowerCase) fullAlphabet += LOWER_CASE_ALPHABET
    if (useLetters && upperCase) fullAlphabet += UPPER_CASE_ALPHABET

    return List(length) { fullAlphabet.random() }.joinToString("")
}