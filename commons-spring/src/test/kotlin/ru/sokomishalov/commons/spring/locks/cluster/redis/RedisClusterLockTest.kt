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
package ru.sokomishalov.commons.spring.locks.cluster.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.After
import org.junit.Before
import redis.embedded.RedisServer
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.spring.locks.cluster.AbstractClusterLockTest
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import ru.sokomishalov.commons.spring.net.randomFreePort

class RedisClusterLockTest : AbstractClusterLockTest() {

    private lateinit var redisServer: RedisServer
    private lateinit var lettuceLockProvider: RedisLettuceLockProvider

    @Before
    fun setUp() {
        val randomPort = randomFreePort()
        redisServer = RedisServer(randomPort)
        redisServer.start()

        lettuceLockProvider = RedisLettuceLockProvider(RedisClient.create(RedisURI.create(LOCALHOST, randomPort)))
    }

    override val provider: LockProvider
        get() = lettuceLockProvider

    @After
    override fun tearDown() {
        super.tearDown()
        redisServer.stop()
    }
}