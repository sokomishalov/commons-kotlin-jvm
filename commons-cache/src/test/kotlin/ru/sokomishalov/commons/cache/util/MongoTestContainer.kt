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

import com.mongodb.ConnectionString
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.testcontainers.containers.GenericContainer

/**
 * @author sokomishalov
 */

class MongoTestContainer : GenericContainer<MongoTestContainer>("mvertes/alpine-mongo")

internal fun createDefaultMongoContainer(): MongoTestContainer {
    return MongoTestContainer().apply {
        withReuse(true)
        withExposedPorts(27017)
    }
}

fun MongoTestContainer.createReactiveMongoClient(): MongoClient {
    return MongoClients.create(ConnectionString("mongodb://${containerIpAddress}:${firstMappedPort}"))
}