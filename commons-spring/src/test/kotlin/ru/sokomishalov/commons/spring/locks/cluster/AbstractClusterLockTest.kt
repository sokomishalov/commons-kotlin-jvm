package ru.sokomishalov.commons.spring.locks.cluster

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration.ofMinutes
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractClusterLockTest {

    protected abstract val provider: LockProvider

    @Test
    fun `Check at least for duration`() {
        val counter = AtomicInteger(0)
        val iterations = 5

        repeat(iterations) {
            runBlocking {
                provider.withClusterLock(lockName = "atLeastForLock", lockAtLeastFor = ofMinutes(10)) {
                    counter.incrementAndGet()
                }
            }
        }

        assertEquals(1, counter.get())
    }

    @Test
    fun `Check at most for duration`() {
        val counter = AtomicInteger(0)
        val iterations = 5

        repeat(iterations) {
            runBlocking {
                provider.withClusterLock(lockName = "atLeastForLock", lockAtMostFor = ofMinutes(10)) {
                    counter.incrementAndGet()
                }
            }
        }

        assertEquals(iterations, counter.get())
    }

    @After
    open fun tearDown() = Unit
}