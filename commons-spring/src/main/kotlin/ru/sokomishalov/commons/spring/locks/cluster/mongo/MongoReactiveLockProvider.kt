package ru.sokomishalov.commons.spring.locks.cluster.mongo

import com.mongodb.ErrorCategory.DUPLICATE_KEY
import com.mongodb.ErrorCategory.fromErrorCode
import com.mongodb.MongoServerException
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoCollection
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitSingle
import org.bson.Document
import ru.sokomishalov.commons.core.common.unit
import ru.sokomishalov.commons.spring.locks.cluster.LockInfo
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import java.util.*

/**
 * @author sokomishalov
 */

class MongoReactiveLockProvider(
        private val client: MongoClient = MongoClients.create(),
        private val databaseName: String = DEFAULT_DB_NAME,
        private val collectionName: String = DEFAULT_COLLECTION_NAME
) : LockProvider {

    companion object {
        private const val DEFAULT_DB_NAME = "clusterLockDb"
        private const val DEFAULT_COLLECTION_NAME = "clusterLock"
        private const val ID_FIELD = "_id"
        private const val LOCKED_UNTIL_FIELD = "lockedUntil"
        private const val LOCKED_AT_FIELD = "lockedAt"
        private const val LOCKED_BY_FIELD = "lockedBy"
    }

    /**
     * There are three possible situations:
     * 1. The lock document does not exist yet - it is inserted - we have the lock
     * 2. The lock document exists and lockUntil before now - it is updated - we have the lock
     * 3. The lock document exists and lockUntil after now - Duplicate key exception is thrown
     */
    override suspend fun tryLock(lockInfo: LockInfo): Boolean {
        return try {
            getCollection()
                    .findOneAndUpdate(
                            and(eq(ID_FIELD, lockInfo.lockName), lte(LOCKED_UNTIL_FIELD, Date())),
                            combine(
                                    set(LOCKED_UNTIL_FIELD, Date.from(lockInfo.lockedUntil.toInstant())),
                                    set(LOCKED_AT_FIELD, Date.from(lockInfo.lockedAt.toInstant())),
                                    set(LOCKED_BY_FIELD, lockInfo.lockedBy)
                            ),
                            FindOneAndUpdateOptions().upsert(true)
                    )
                    .awaitFirstOrElse { null }
                    .unit()
            true
        } catch (e: MongoServerException) {
            when (fromErrorCode(e.code)) {
                DUPLICATE_KEY -> false
                else -> throw e
            }
        }
    }

    override suspend fun release(lockInfo: LockInfo) {
        return getCollection()
                .findOneAndUpdate(
                        eq(ID_FIELD, lockInfo.lockName),
                        combine(set(LOCKED_UNTIL_FIELD, Date.from(lockInfo.lockedUntil.toInstant())))
                )
                .awaitSingle()
                .unit()
    }

    private fun getCollection(): MongoCollection<Document> {
        return client
                .getDatabase(databaseName)
                .getCollection(collectionName)
    }
}