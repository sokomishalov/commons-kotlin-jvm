@file:Suppress("unused")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlFactory

/**
 * @author sokomishalov
 */

val XML_OBJECT_MAPPER: ObjectMapper = buildComplexObjectMapper(factory = XmlFactory())