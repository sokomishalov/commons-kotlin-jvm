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
package ru.sokomishalov.commons.distributed.locks.mongo

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
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.bson.Document
import ru.sokomishalov.commons.core.common.unit
import ru.sokomishalov.commons.distributed.locks.DistributedLockInfo
import ru.sokomishalov.commons.distributed.locks.DistributedLockProvider
import java.util.*

/**
 * @author sokomishalov
 */

class MongoReactiveDistributedLockProvider(
        private val client: MongoClient = MongoClients.create(),
        private val databaseName: String = DEFAULT_DB_NAME,
        private val collectionName: String = DEFAULT_COLLECTION_NAME
) : DistributedLockProvider {

    companion object {
        private const val DEFAULT_DB_NAME = "distributedLockDb"
        private const val DEFAULT_COLLECTION_NAME = "distributedLock"
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
    override suspend fun tryLock(distributedLockInfo: DistributedLockInfo): Boolean {
        return try {
            getCollection()
                    .findOneAndUpdate(
                            and(eq(ID_FIELD, distributedLockInfo.lockName), lte(LOCKED_UNTIL_FIELD, Date())),
                            combine(
                                    set(LOCKED_UNTIL_FIELD, Date.from(distributedLockInfo.lockedUntil.toInstant())),
                                    set(LOCKED_AT_FIELD, Date.from(distributedLockInfo.lockedAt.toInstant())),
                                    set(LOCKED_BY_FIELD, distributedLockInfo.lockedBy)
                            ),
                            FindOneAndUpdateOptions().upsert(true)
                    )
                    .awaitFirstOrNull()
                    .unit()
            true
        } catch (e: MongoServerException) {
            when (fromErrorCode(e.code)) {
                DUPLICATE_KEY -> false
                else -> throw e
            }
        }
    }

    override suspend fun release(distributedLockInfo: DistributedLockInfo) {
        return getCollection()
                .findOneAndUpdate(
                        eq(ID_FIELD, distributedLockInfo.lockName),
                        combine(set(LOCKED_UNTIL_FIELD, Date.from(distributedLockInfo.lockedUntil.toInstant())))
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