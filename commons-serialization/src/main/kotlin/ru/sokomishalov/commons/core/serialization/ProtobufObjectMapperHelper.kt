@file:Suppress("unused")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory

/**
 * @author sokomishalov
 */

val PROTOBUF_OBJECT_MAPPER: ObjectMapper = buildComplexObjectMapper(factory = ProtobufFactory())