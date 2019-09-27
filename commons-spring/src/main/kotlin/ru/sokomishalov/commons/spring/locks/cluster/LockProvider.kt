@file:Suppress("unused")

package ru.sokomishalov.commons.spring.locks.cluster

/**
 * @author sokomishalov
 */
interface LockProvider {

    suspend fun tryLock(lockInfo: LockInfo): Boolean

    suspend fun release(lockInfo: LockInfo)
}