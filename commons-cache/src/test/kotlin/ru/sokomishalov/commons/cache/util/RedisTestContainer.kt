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