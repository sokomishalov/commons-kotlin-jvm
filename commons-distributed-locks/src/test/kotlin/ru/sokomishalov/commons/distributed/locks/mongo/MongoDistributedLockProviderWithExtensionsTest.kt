package ru.sokomishalov.commons.distributed.locks.mongo

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.distributed.locks.AbstractClusterLockWithExtensionsTest
import ru.sokomishalov.commons.distributed.locks.DistributedLockProvider
import ru.sokomishalov.commons.util.MongoTestContainer
import ru.sokomishalov.commons.util.createDefaultMongoContainer
import ru.sokomishalov.commons.util.createReactiveMongoClient

/**
 * @author sokomishalov
 */
class MongoDistributedLockProviderWithExtensionsTest : AbstractClusterLockWithExtensionsTest() {

    companion object : Loggable {
        @get:ClassRule
        val mongo: MongoTestContainer = createDefaultMongoContainer()

        @AfterClass
        @JvmStatic
        fun stop() = mongo.stop()
    }

    override val providerDistributed: DistributedLockProvider by lazy {
        mongo.start()
        logInfo(mongo.logs)
        MongoReactiveDistributedLockProvider(client = mongo.createReactiveMongoClient())
    }
}