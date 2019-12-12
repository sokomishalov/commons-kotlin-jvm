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
package ru.sokomishalov.commons.cache.util

import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.testcontainers.containers.GenericContainer
import java.time.Duration

/**
 * @author sokomishalov
 */

class RedisTestContainer : GenericContainer<RedisTestContainer>("redis:alpine")

internal fun createDefaultRedisContainer(): RedisTestContainer {
    return RedisTestContainer().apply {
        withReuse(true)
        withExposedPorts(6379)
    }
}

fun RedisTestContainer.createRedisClient(timeout: Duration = Duration.ofSeconds(1)): RedisClient {
    return RedisClient.create(RedisURI(containerIpAddress, firstMappedPort, timeout))
}