@file:Suppress("unused")

package ru.sokomishalov.commons.core.serialization

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvFactory

/**
 * @author sokomishalov
 */

val CSV_OBJECT_MAPPER: ObjectMapper = buildComplexObjectMapper(factory = CsvFactory())