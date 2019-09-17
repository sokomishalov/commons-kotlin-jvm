@file:Suppress("unused")

package ru.sokomishalov.commons.core.consts

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

/**
 * @author sokomishalov
 */
const val EMPTY = ""

val DEFAULT_DATE_FORMATTER: DateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss")