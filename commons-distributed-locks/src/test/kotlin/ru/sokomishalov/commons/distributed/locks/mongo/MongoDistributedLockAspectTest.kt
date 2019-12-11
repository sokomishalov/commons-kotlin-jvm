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
package ru.sokomishalov.commons.distributed.locks.mongo

import org.junit.AfterClass
import org.junit.ClassRule
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.distributed.locks.AbstractDistributedLockAspectTest
import ru.sokomishalov.commons.util.MongoTestContainer
import ru.sokomishalov.commons.util.createDefaultMongoContainer
import ru.sokomishalov.commons.util.createReactiveMongoClient


/**
 * @author sokomishalov
 */

@ContextConfiguration(classes = [AbstractDistributedLockAspectTest.AspectConfig::class], initializers = [MongoDistributedLockAspectTest.Initializer::class])
class MongoDistributedLockAspectTest : AbstractDistributedLockAspectTest() {

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