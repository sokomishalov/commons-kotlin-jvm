package ru.sokomishalov.commons.spring.locks.cluster

import java.time.ZonedDateTime

/**
 * @author sokomishalov
 */

data class LockInfo(
        val lockName: String,
        val lockedBy: String,
        val lockedAt: ZonedDateTime,
        val lockedUntil: ZonedDateTime
)