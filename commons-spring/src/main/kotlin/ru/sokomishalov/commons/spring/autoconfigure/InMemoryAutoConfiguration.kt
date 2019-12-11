@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import ru.sokomishalov.commons.cache.CacheService
import ru.sokomishalov.commons.cache.inmemory.ConcurrentMapCacheService

/**
 * @author sokomishalov
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(LOWEST_PRECEDENCE)
class InMemoryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheService::class)
    fun concurrentMapCacheManager(): CacheService {
        return ConcurrentMapCacheService()
    }
}