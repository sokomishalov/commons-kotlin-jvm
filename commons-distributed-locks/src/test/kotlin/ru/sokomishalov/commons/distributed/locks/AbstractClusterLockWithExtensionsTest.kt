package ru.sokomishalov.commons.distributed.locks

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration.ofMinutes
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author sokomishalov
 */
abstract class AbstractClusterLockWithExtensionsTest {

    protected abstract val providerDistributed: DistributedLockProvider

    @Test
    fun `Check at least for duration`() {
        val counter = AtomicInteger(0)
        val iterations = 5

        repeat(iterations) {
            runBlocking {
                providerDistributed.withClusterLock(lockName = "lockAtLeastFor", lockAtLeastFor = ofMinutes(10)) {
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
                providerDistributed.withClusterLock(lockName = "lockAtMostFor", lockAtMostFor = ofMinutes(10)) {
                    counter.incrementAndGet()
                }
            }
        }

        assertEquals(iterations, counter.get())
    }
}