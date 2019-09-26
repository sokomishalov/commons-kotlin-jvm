@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.sokomishalov.commons.core.string

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * @author sokomishalov
 */

@UseExperimental(ExperimentalContracts::class)
inline fun CharSequence?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }
    return (this == null || this.isBlank()).not()
}