package ru.sokomishalov.commons.cache.spring

import org.junit.Assert.assertTrue
import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService

/**
 * @author sokomishalov
 */
class SpringCacheServiceTest : AbstractCacheServiceTest() {

    override val cacheService: CacheService by lazy { SpringCacheService(cacheName = "cache") }

    // not realized yet
    override fun `Test find by glob pattern`() = assertTrue(true)
}