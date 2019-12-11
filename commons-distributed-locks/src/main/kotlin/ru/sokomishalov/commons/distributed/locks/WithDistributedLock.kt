package ru.sokomishalov.commons.distributed.locks

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

/**
 * @author sokomishalov
 */

@Target(FUNCTION, PROPERTY_SETTER, PROPERTY_GETTER)
@Retention(RUNTIME)
annotation class WithDistributedLock(

        // name of lock
        val lockName: String = "lock",

        // will hold the lock for this duration, at a minimum
        val lockAtLeastForMs: Long = 0,

        // will hold the lock for this duration, at a maximum
        val lockAtMostForMs: Long = 0
)