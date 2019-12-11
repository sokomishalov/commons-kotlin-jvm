package ru.sokomishalov.commons.cache.util

import org.tarantool.TarantoolClient
import org.tarantool.TarantoolClientConfig
import org.tarantool.TarantoolClientImpl
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.MountableFile.forClasspathResource

class TarantoolTestContainer : GenericContainer<TarantoolTestContainer>("tarantool/tarantool:1.10.2")

fun createDefaultTarantoolContainer(): TarantoolTestContainer {
    return TarantoolTestContainer().apply {
        withReuse(true)
        copyToFileContainerPathMap = mapOf(forClasspathResource("tarantool/app.lua") to "/opt/tarantool/")
        withExposedPorts(3301)
        withCommand("tarantool", "/opt/tarantool/app.lua")
    }
}

fun TarantoolTestContainer.createTarantoolClient(config: TarantoolClientConfig = TarantoolClientConfig()): TarantoolClient {
    return TarantoolClientImpl("${containerIpAddress}:${firstMappedPort}", config)
}