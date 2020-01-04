@file:Suppress("unused", "EXPERIMENTAL_API_USAGE")

package ru.sokomishalov.commons.core.scheduled

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.delay as sleep

fun CoroutineScope.runScheduled(
        context: CoroutineContext = EmptyCoroutineContext,
        delay: Duration = Duration.ZERO,
        interval: Duration = Duration.ZERO,
        action: suspend () -> Unit
): Job = launch(newCoroutineContext(context)) {
    sleep(delay.toMillis())
    if (interval > Duration.ZERO) {
        while (true) {
            action()
            sleep(interval.toMillis())
        }
    } else {
        action()
    }
}