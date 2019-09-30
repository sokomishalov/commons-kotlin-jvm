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
@file:Suppress("unused", "RemoveExplicitTypeArguments")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext


/**
 * @author sokomishalov
 */

suspend inline fun ObjectMapper.aReadTree(content: String): JsonNode = withContext(IO) {
    readTree(content)
}

suspend inline fun <reified T> ObjectMapper.aReadValue(content: String): T = withContext(IO) {
    readValue<T>(content)
}

suspend inline fun <reified T> ObjectMapper.aConvertValue(from: String): T = withContext(IO) {
    convertValue<T>(from)
}