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
@file:Suppress("unused", "UNCHECKED_CAST")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.DeserializationFeature.*
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE
import com.fasterxml.jackson.databind.SerializationFeature.*
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule


/**
 * @author sokomishalov
 */

fun buildComplexObjectMapper(
        factory: JsonFactory? = null,
        namingStrategy: PropertyNamingStrategy = LOWER_CAMEL_CASE
): ObjectMapper {
    return ObjectMapper(factory)
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .registerModule(Jdk8Module())
            .enable(
                    READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE,
                    READ_ENUMS_USING_TO_STRING,
                    ACCEPT_SINGLE_VALUE_AS_ARRAY,
                    ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                    ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT
            )
            .enable(
                    ACCEPT_CASE_INSENSITIVE_ENUMS
            )
            .enable(
                    WRITE_ENUMS_USING_TO_STRING
            )
            .disable(
                    FAIL_ON_EMPTY_BEANS,
                    WRITE_DATES_AS_TIMESTAMPS
            )
            .disable(
                    ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                    FAIL_ON_UNKNOWN_PROPERTIES
            )
            .setSerializationInclusion(NON_NULL)
            .setPropertyNamingStrategy(namingStrategy)
}