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