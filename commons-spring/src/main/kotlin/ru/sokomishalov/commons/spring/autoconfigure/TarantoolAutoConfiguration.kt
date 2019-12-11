@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.tarantool.TarantoolClient
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.tarantool.TarantoolCacheService

/**
 * @author sokomishalov
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(TarantoolClient::class)
@AutoConfigureOrder(HIGHEST_PRECEDENCE)
class TarantoolAutoConfiguration {

    @Bean
    @ConditionalOnBean(TarantoolClient::class)
    @ConditionalOnMissingBean(CacheService::class)
    fun tarantoolCacheManager(client: TarantoolClient): CacheService {
        return TarantoolCacheService(client = client)
    }
}