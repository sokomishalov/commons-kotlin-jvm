@file:Suppress("EXPERIMENTAL_API_USAGE")

package ru.sokomishalov.commons.core.collections

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.CoroutineContext

@PublishedApi
internal fun CoroutineScope?.plusContext(context: CoroutineContext): CoroutineContext = when (this) {
    null -> context
    else -> newCoroutineContext(context)
}