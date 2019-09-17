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