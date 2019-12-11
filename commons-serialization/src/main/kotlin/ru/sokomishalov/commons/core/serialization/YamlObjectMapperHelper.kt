@file:Suppress("unused")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

/**
 * @author sokomishalov
 */

val YAML_OBJECT_MAPPER: ObjectMapper = buildComplexObjectMapper(factory = YAMLFactory())