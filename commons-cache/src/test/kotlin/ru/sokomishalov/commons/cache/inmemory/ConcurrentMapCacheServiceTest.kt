package ru.sokomishalov.commons.cache.inmemory

import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService


/**
 * @author sokomishalov
 */
class ConcurrentMapCacheServiceTest : AbstractCacheServiceTest() {

    override val cacheService: CacheService by lazy { ConcurrentMapCacheService() }

}