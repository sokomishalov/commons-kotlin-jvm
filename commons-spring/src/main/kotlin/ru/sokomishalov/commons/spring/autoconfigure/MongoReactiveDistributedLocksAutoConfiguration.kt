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
@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import ru.sokomishalov.commons.distributed.locks.DistributedLockProvider
import ru.sokomishalov.commons.distributed.locks.mongo.MongoReactiveDistributedLockProvider

/**
 * @author sokomishalov
 */
@Configuration
@ConditionalOnClass(DistributedLockProvider::class, MongoClient::class)
@AutoConfigureOrder(HIGHEST_PRECEDENCE)
class MongoReactiveDistributedLocksAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DistributedLockProvider::class)
    @ConditionalOnBean(MongoClient::class)
    fun reactiveMongoDistributedLockProvider(client: MongoClient): DistributedLockProvider {
        return MongoReactiveDistributedLockProvider(client = client)
    }

}