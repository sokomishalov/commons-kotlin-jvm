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
package ru.sokomishalov.commons.cache.redis

import org.junit.AfterClass
import org.junit.ClassRule
import ru.sokomishalov.commons.cache.AbstractCacheServiceTest
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.util.RedisTestContainer
import ru.sokomishalov.commons.cache.util.createDefaultRedisContainer
import ru.sokomishalov.commons.cache.util.createRedisClient
import ru.sokomishalov.commons.core.log.Loggable

/**
 * @author sokomishalov
 */
class RedisCacheServiceTest : AbstractCacheServiceTest() {

    companion object : Loggable {
        @get:ClassRule
        val redis: RedisTestContainer = createDefaultRedisContainer()

        @AfterClass
        @JvmStatic
        fun stop() = redis.stop()
    }

    override val cacheService: CacheService by lazy {
        redis.start()
        logInfo(redis.logs)
        RedisCacheService(client = redis.createRedisClient())
    }
}