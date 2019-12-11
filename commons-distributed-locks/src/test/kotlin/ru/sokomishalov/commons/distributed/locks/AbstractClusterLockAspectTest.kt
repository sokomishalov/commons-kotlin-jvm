package ru.sokomishalov.commons.distributed.locks

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.concurrent.atomic.AtomicInteger


/**
 * @author sokomishalov
 */
@RunWith(SpringJUnit4ClassRunner::class)
@ComponentScan("ru.sokomishalov.commons.cluster.locks")
@EnableAspectJAutoProxy
abstract class AbstractClusterLockAspectTest {

    @Autowired
    lateinit var someBean: AspectConfig.SomeBean

    @Test
    fun `Check at least for duration`() {
        val counter = AtomicInteger(0)
        val iterations = 5

        repeat(iterations) {
            someBean.incrementCounterAtLeast(counter)
        }

        assertEquals(1, counter.get())
    }

    @Test
    fun `Check at most for duration`() {
        val counter = AtomicInteger(0)
        val iterations = 5

        repeat(iterations) {
            someBean.incrementCounterAtMost(counter)
        }

        assertEquals(iterations, counter.get())
    }

    @TestConfiguration
    open class AspectConfig {

        @Bean
        open fun aspect(distributedLockProvider: DistributedLockProvider): WithDistributedLockAspect {
            return WithDistributedLockAspect(distributedLockProvider)
        }

        @Component
        open class SomeBean {

            @WithDistributedLock(lockAtLeastForMs = 600_000, lockAtMostForMs = 600_000)
            open fun incrementCounterAtLeast(counter: AtomicInteger) {
                counter.incrementAndGet()
            }

            @WithDistributedLock(lockAtMostForMs = 600_000)
            open fun incrementCounterAtMost(counter: AtomicInteger) {
                counter.incrementAndGet()
            }
        }
    }
}