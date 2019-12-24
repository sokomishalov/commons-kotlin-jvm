@file:Suppress("NOTHING_TO_INLINE", "unused")

package ru.sokomishalov.commons.core.numbers

/**
 * @author sokomishalov
 */

const val MULTIPLIER: Int = 1024

// kilobyte
inline val Int.kb: Long get() = this.toLong() * MULTIPLIER
inline val Long.kb: Long get() = this * MULTIPLIER

// megabyte
inline val Int.mb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER
inline val Long.mb: Long get() = this * MULTIPLIER * MULTIPLIER

// gigabyte
inline val Int.gb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER * MULTIPLIER
inline val Long.gb: Long get() = this * MULTIPLIER * MULTIPLIER * MULTIPLIER

// terabyte
inline val Int.tb: Long get() = this.toLong() * MULTIPLIER * MULTIPLIER * MULTIPLIER * MULTIPLIER
inline val Long.tb: Long get() = this * MULTIPLIER * MULTIPLIER * MULTIPLIER * MULTIPLIER