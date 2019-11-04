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
package ru.sokomishalov.commons.spring.locks.cluster.mongo

import com.mongodb.reactivestreams.client.MongoClients
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION
import de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6
import org.junit.After
import org.junit.Before
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.spring.locks.cluster.AbstractClusterLockTest
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import ru.sokomishalov.commons.spring.net.randomFreePort


/**
 * @author sokomishalov
 */
class MongoClusterLockTest : AbstractClusterLockTest() {

    companion object {
        const val DATABASE = "admin"
    }

    private lateinit var mongoDaemon: MongodProcess
    private lateinit var client: LockProvider

    @Before
    fun setUp() {
        val randomPort = randomFreePort()

        mongoDaemon = MongodStarter
                .getDefaultInstance()
                .prepare(MongodConfigBuilder()
                        .version(PRODUCTION)
                        .net(Net(LOCALHOST, randomPort, localhostIsIPv6()))
                        .build()
                )
                .start()

        client = MongoReactiveLockProvider(client = MongoClients.create("mongodb://$LOCALHOST:$randomPort/$DATABASE"))
    }


    override val provider: LockProvider
        get() = client

    @After
    fun tearDown() {
        mongoDaemon.stop()
    }
}