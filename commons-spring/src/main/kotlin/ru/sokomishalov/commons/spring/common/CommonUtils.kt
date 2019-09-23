@file:Suppress("unused")

package ru.sokomishalov.commons.spring.common

import org.springframework.http.ResponseEntity

/**
 * @author sokomishalov
 */

inline fun <reified T> T?.toResponseEntity(): ResponseEntity<T> = when {
    this == null -> ResponseEntity.ok().build()
    else -> ResponseEntity.ok(this)
}