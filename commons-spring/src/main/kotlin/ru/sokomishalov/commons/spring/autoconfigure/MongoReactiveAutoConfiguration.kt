@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import ru.sokomishalov.commons.spring.locks.cluster.mongo.MongoReactiveLockProvider

/**
 * @author sokomishalov
 */
@Configuration
@ConditionalOnClass(MongoClient::class)
class MongoReactiveAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LockProvider::class)
    @ConditionalOnBean(MongoClient::class)
    fun reactiveMongoClusterLockProvider(client: MongoClient): LockProvider =
            MongoReactiveLockProvider(client)

}