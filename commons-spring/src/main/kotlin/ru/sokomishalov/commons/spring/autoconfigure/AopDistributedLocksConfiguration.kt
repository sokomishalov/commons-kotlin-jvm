@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package ru.sokomishalov.commons.spring.autoconfigure

import org.aspectj.lang.reflect.Advice
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.sokomishalov.commons.distributed.locks.DistributedLockProvider
import ru.sokomishalov.commons.distributed.locks.WithDistributedLockAspect

@Configuration(proxyBeanMethods = true)
@ConditionalOnClass(Advice::class, DistributedLockProvider::class)
class AopDistributedLocksConfiguration {

    @Bean
    @ConditionalOnBean(DistributedLockProvider::class)
    fun aspect(lockProvider: DistributedLockProvider): WithDistributedLockAspect {
        return WithDistributedLockAspect(lockProvider)
    }
}