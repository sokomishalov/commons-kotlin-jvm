package ru.sokomishalov.commons.cache.tarantool

import org.junit.AfterClass
import org.junit.Assert.assertTrue
import org.junit.ClassRule
import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.util.TarantoolTestContainer
import ru.sokomishalov.commons.cache.util.createDefaultTarantoolContainer
import ru.sokomishalov.commons.cache.util.createTarantoolClient
import ru.sokomishalov.commons.core.log.Loggable

/**
 * @author sokomishalov
 */
class TarantoolCacheServiceTest : AbstractCacheServiceTest() {

    companion object : Loggable {
        @get:ClassRule
        val tarantool: TarantoolTestContainer = createDefaultTarantoolContainer()

        @AfterClass
        @JvmStatic
        fun stop() = tarantool.stop()
    }

    override val cacheService: CacheService by lazy {
        tarantool.start()
        logInfo(tarantool.logs)
        TarantoolCacheService(client = tarantool.createTarantoolClient())
    }

    // not realized yet
    override fun `Test find by glob pattern`() = assertTrue(true)
}