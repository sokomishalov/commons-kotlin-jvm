@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import io.lettuce.core.RedisClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sokomishalov.commons.spring.locks.cluster.LockProvider
import ru.sokomishalov.commons.spring.locks.cluster.redis.RedisLettuceLockProvider

/**
 * @author sokomishalov
 */
@Configuration
@ConditionalOnClass(RedisClient::class)
class RedisLettuceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(LockProvider::class)
    @ConditionalOnBean(RedisClient::class)
    fun reactiveMongoClusterLockProvider(client: RedisClient): LockProvider =
            RedisLettuceLockProvider(client = client)
}