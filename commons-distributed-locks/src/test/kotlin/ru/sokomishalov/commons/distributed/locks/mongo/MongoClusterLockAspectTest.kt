package ru.sokomishalov.commons.distributed.locks.mongo

import org.junit.AfterClass
import org.junit.ClassRule
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.distributed.locks.AbstractClusterLockAspectTest
import ru.sokomishalov.commons.util.MongoTestContainer
import ru.sokomishalov.commons.util.createDefaultMongoContainer
import ru.sokomishalov.commons.util.createReactiveMongoClient


/**
 * @author sokomishalov
 */

@ContextConfiguration(classes = [AbstractClusterLockAspectTest.AspectConfig::class], initializers = [MongoClusterLockAspectTest.Initializer::class])
class MongoClusterLockAspectTest : AbstractClusterLockAspectTest() {

    companion object : Loggable {
        @get:ClassRule
        val mongo: MongoTestContainer = createDefaultMongoContainer()

        @AfterClass
        @JvmStatic
        fun stop() = mongo.stop()
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(context: ConfigurableApplicationContext) {
            mongo.start()
            logInfo(mongo.logs)
            context.beanFactory.registerSingleton("lockProvider", MongoReactiveDistributedLockProvider(client = mongo.createReactiveMongoClient()))
        }
    }
}