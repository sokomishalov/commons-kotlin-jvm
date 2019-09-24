@file:Suppress("unused")

package ru.sokomishalov.commons.spring.serialization

import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER

/**
 * @author sokomishalov
 */

val JACKSON_ENCODER = Jackson2JsonEncoder(OBJECT_MAPPER)

val JACKSON_DECODER = Jackson2JsonDecoder(OBJECT_MAPPER)