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
@file:Suppress("DEPRECATION")

package ru.sokomishalov.commons.cache.mongo

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.ConnectionString
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.Updates.set
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.Document
import org.bson.conversions.Bson
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.core.reactor.await
import ru.sokomishalov.commons.core.reactor.awaitUnit
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import ru.sokomishalov.commons.core.string.convertGlobToRegex
import java.time.temporal.TemporalAmount
import java.util.Base64.getDecoder
import java.util.Base64.getEncoder

/**
 * @author sokomishalov
 */
class MongoCacheService(
        override val cacheName: String = "cache",
        override val mapper: ObjectMapper = OBJECT_MAPPER,
        private val host: String = LOCALHOST,
        private val port: Int = 27017,
        private val databaseName: String = "lockDB",
        private val client: MongoClient = MongoClients.create(ConnectionString("mongodb://${host}:${port}/${databaseName}"))
) : CacheService {

    companion object : Loggable {
        private const val ID_FIELD = "_id"
        private const val RAW_VALUE_FIELD = "rawValue"
    }

    private val collection: MongoCollection<Document> get() = client.getDatabase(databaseName).getCollection(cacheName)

    override suspend fun getRaw(key: String): ByteArray? {
        return collection
                .find(key.buildKeyBson())
                .toMono()
                .await()
                ?.get(RAW_VALUE_FIELD)
                ?.let { (it as String).decodeBase64() }
    }

    override suspend fun putRaw(key: String, value: ByteArray) {
        collection
                .findOneAndUpdate(key.buildKeyBson(), value.buildValueBson(), FindOneAndUpdateOptions().upsert(true))
                .toMono()
                .awaitUnit()
    }

    override suspend fun expire(key: String, ttl: TemporalAmount) {
        logWarn("expire() is unsupported")
    }

    override suspend fun delete(key: String) {
        collection
                .deleteOne(key.buildKeyBson())
                .toMono()
                .awaitUnit()
    }

    override suspend fun findKeys(glob: String): List<String> {
        return collection
                .find(eq(ID_FIELD, glob.convertGlobToRegex().toPattern()))
                .toFlux()
                .await()
                .mapNotNull { it?.get(ID_FIELD) as String? }
    }

    override suspend fun deleteAll() {
        collection.drop().awaitFirstOrNull()
    }

    private fun String.buildKeyBson(): Bson = eq(ID_FIELD, this)
    private fun ByteArray.buildValueBson(): Bson = set(RAW_VALUE_FIELD, this.encodeBase64())

    private fun ByteArray.encodeBase64() = getEncoder().encodeToString(this)
    private fun String.decodeBase64() = getDecoder().decode(this)

    // override some methods for better performance
}