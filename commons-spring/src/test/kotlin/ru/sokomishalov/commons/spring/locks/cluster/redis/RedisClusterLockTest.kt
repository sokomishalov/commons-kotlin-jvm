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