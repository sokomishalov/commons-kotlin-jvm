package ru.sokomishalov.commons.distributed.locks.redis

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.junit.AfterClass
import org.junit.ClassRule
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.distributed.locks.AbstractClusterLockAspectTest
import ru.sokomishalov.commons.util.RedisTestContainer
import ru.sokomishalov.commons.util.createDefaultRedisContainer


/**
 * @author sokomishalov
 */

@ContextConfiguration(classes = [AbstractClusterLockAspectTest.AspectConfig::class], initializers = [RedisClusterLockAspectTest.Initializer::class])
class RedisClusterLockAspectTest : AbstractClusterLockAspectTest() {

    companion object : Loggable {
        @get:ClassRule
        val redis: RedisTestContainer = createDefaultRedisContainer()

        @AfterClass
        @JvmStatic
        fun stop() = redis.stop()
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(context: ConfigurableApplicationContext) {
            redis.start()
            logInfo(redis.logs)
            context.beanFactory.registerSingleton("lockProvider", RedisLettuceDistributedLockProvider(client = RedisClient.create(RedisURI().apply {
                host = redis.containerIpAddress
                port = redis.firstMappedPort
            })))
        }
    }
}