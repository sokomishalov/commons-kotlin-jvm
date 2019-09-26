@file:Suppress("unused", "NOTHING_TO_INLINE")

package ru.sokomishalov.commons.core.collections


/**
 * @author sokomishalov
 */

inline fun <K, V> Map<out K, V>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}

inline fun <K, V> Map<out K, V>?.isNotNullOrEmpty(): Boolean {
    return (this == null || isEmpty()).not()
}

infix fun <K, V> Map<K, V>.containsEntryFrom(other: Map<K, V>): Boolean {
    return this.entries.intersect(other.entries).isNullOrEmpty().not()
}
