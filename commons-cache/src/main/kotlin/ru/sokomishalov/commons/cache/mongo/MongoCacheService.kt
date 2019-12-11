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

    override suspend fun getRaw(key: String): String? {
        return getCollection()
                .find(key.buildKeyBson())
                .toMono()
                .await()
                ?.get(RAW_VALUE_FIELD) as String?
    }

    override suspend fun putRaw(key: String, value: String) {
        getCollection()
                .findOneAndUpdate(key.buildKeyBson(), value.buildValueBson(), FindOneAndUpdateOptions().upsert(true))
                .toMono()
                .awaitUnit()
    }

    override suspend fun expire(key: String, ttl: TemporalAmount) {
        logWarn("expire() is unsupported")
    }

    override suspend fun delete(key: String) {
        getCollection()
                .deleteOne(key.buildKeyBson())
                .toMono()
                .awaitUnit()
    }

    override suspend fun findKeys(glob: String): List<String> {
        return getCollection()
                .find(eq(ID_FIELD, glob.convertGlobToRegex().toPattern()))
                .toFlux()
                .await()
                .mapNotNull { it?.get(ID_FIELD) as String? }
    }

    override suspend fun deleteAll() {
        getCollection().drop().awaitFirstOrNull()
    }

    private fun String.buildKeyBson(): Bson = eq(ID_FIELD, this)
    private fun String.buildValueBson(): Bson = set(RAW_VALUE_FIELD, this)

    private fun getCollection(): MongoCollection<Document> = client.getDatabase(databaseName).getCollection(cacheName)

    // override some methods for better performance
}