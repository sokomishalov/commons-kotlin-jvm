package ru.sokomishalov.commons.cache.tarantool

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.future.await
import org.tarantool.TarantoolClient
import org.tarantool.TarantoolClientConfig
import org.tarantool.TarantoolClientImpl
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.core.common.unit
import ru.sokomishalov.commons.core.consts.LOCALHOST
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.core.serialization.OBJECT_MAPPER
import java.time.temporal.TemporalAmount

/**
 * @author sokomishalov
 */

class TarantoolCacheService(
        override val cacheName: String = "cache",
        override val mapper: ObjectMapper = OBJECT_MAPPER,
        private val host: String = LOCALHOST,
        private val port: Int = 3301,
        private val client: TarantoolClient = TarantoolClientImpl("${host}:${port}", TarantoolClientConfig())
) : CacheService, Loggable {

    companion object {
        const val BOX_SPACE = "box.space."

        private const val VALUE_INDEX = 1

        private const val SELECT_OP = ":select"
        private const val WRITE_OP = ":put"
        private const val DELETE_OP = ":delete"
        private const val TRUNCATE_OP = ":truncate"
    }

    private fun String.operation() = "$BOX_SPACE${cacheName}${this}"

    override suspend fun getRaw(key: String): String? {
        val result = client.composableAsyncOps().call(SELECT_OP.operation(), key.addPrefix()).await()
        val tuple = result.firstOrNull() as List<*>?
        return when {
            tuple.isNullOrEmpty() -> null
            else -> tuple[VALUE_INDEX] as String
        }
    }

    override suspend fun putRaw(key: String, value: String) {
        client.composableAsyncOps().call(WRITE_OP.operation(), listOf(key.addPrefix(), value)).await().unit()
    }

    override suspend fun expire(key: String, ttl: TemporalAmount) {
        logWarn("expire() is unsupported")
    }

    override suspend fun delete(key: String) {
        client.composableAsyncOps().call(DELETE_OP.operation(), key.addPrefix()).await()
    }

    override suspend fun findKeys(glob: String): List<String> {
        logWarn("findKeys() is unsupported")
        return emptyList()
    }

    override suspend fun deleteAll() {
        client.composableAsyncOps().call(TRUNCATE_OP.operation()).await()
    }

    // override some methods for better performance
}