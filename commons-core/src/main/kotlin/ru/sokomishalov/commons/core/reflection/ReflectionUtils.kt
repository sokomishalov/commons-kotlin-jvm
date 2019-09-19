@file:Suppress("unused")

package ru.sokomishalov.commons.core.reflection

import kotlin.reflect.full.companionObject

/**
 * @author sokomishalov
 */
fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}