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

        client = MongoReactiveLockProvider(MongoClients.create("mongodb://$LOCALHOST:$randomPort/$DATABASE"))
    }


    override val provider: LockProvider
        get() = client

    @After
    override fun tearDown() {
        super.tearDown()
        mongoDaemon.stop()
    }
}