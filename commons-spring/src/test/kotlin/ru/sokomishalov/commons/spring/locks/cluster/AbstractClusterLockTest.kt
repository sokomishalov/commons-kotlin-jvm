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