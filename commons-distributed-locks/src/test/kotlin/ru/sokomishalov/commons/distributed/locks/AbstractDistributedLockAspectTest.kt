/**
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@ComponentScan("ru.sokomishalov.commons.distributed.locks")
@EnableAspectJAutoProxy
abstract class AbstractDistributedLockAspectTest {

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