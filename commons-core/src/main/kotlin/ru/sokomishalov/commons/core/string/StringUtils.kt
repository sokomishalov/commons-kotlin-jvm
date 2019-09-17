@file:Suppress("unused")

package ru.sokomishalov.commons.core.string

/**
 * @author sokomishalov
 */

fun CharSequence?.isNotNullOrBlank(): Boolean = this.isNullOrBlank().not()