package ru.sokomishalov.commons.cache.mongo

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.util.MongoTestContainer
import ru.sokomishalov.commons.cache.util.createDefaultMongoContainer
import ru.sokomishalov.commons.cache.util.createReactiveMongoClient
import ru.sokomishalov.commons.core.log.Loggable

/**
 * @author sokomishalov
 */
class MongoCacheServiceTest : AbstractCacheServiceTest() {

    companion object : Loggable {
        @get:ClassRule
        val mongo: MongoTestContainer = createDefaultMongoContainer()

        @AfterClass
        @JvmStatic
        fun stop() = mongo.stop()
    }

    override val cacheService: CacheService by lazy {
        mongo.start()
        logInfo(mongo.logs)
        MongoCacheService(client = mongo.createReactiveMongoClient())
    }
}