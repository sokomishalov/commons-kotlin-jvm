/**
 * Copyright 2019-2019 the original author or authors.
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

package ru.sokomishalov.commons.core.consts

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * @author sokomishalov
 */
const val EMPTY = ""
const val LOCALHOST = "localhost"

val DEFAULT_DATE_FORMATTER: DateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss")

val XML_LIKE_REGEX: Regex = "(?s).*(<(\\w+)[^>]*>.*</\\2>|<(\\w+)[^>]*/>).*".toRegex(IGNORE_CASE)
val JSON_LIKE_REGEX: Regex = "[{\\[]([,:{}\\[\\]0-9.\\-+Eaeflnr-u \\n\\r\\t]|\".*?\")+[}\\]]".toRegex(IGNORE_CASE)
val EMAIL_REGEX: Regex = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+".toRegex(IGNORE_CASE)