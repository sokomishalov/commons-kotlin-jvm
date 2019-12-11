package ru.sokomishalov.commons.distributed.locks.redis

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.distributed.locks.AbstractClusterLockWithExtensionsTest
import ru.sokomishalov.commons.distributed.locks.DistributedLockProvider
import ru.sokomishalov.commons.util.RedisTestContainer
import ru.sokomishalov.commons.util.createDefaultRedisContainer
import ru.sokomishalov.commons.util.createRedisClient

/**
 * @author sokomishalov
 */
class RedisDistributedLockProviderWithExtensionsTest : AbstractClusterLockWithExtensionsTest() {

    companion object : Loggable {
        @get:ClassRule
        val redis: RedisTestContainer = createDefaultRedisContainer()

        @AfterClass
        @JvmStatic
        fun stop() = redis.stop()
    }

    override val providerDistributed: DistributedLockProvider by lazy {
        redis.start()
        logInfo(redis.logs)
        RedisLettuceDistributedLockProvider(client = redis.createRedisClient())
    }
}