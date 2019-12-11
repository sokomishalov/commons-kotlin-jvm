package ru.sokomishalov.commons.distributed.locks

import kotlinx.coroutines.runBlocking
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.time.Duration.ofMillis

@Aspect
class WithDistributedLockAspect(private val distributedLockProvider: DistributedLockProvider) {

    @Around("@annotation(ru.sokomishalov.commons.cluster.locks.WithClusterLock) && execution(* *.*(..))")
    fun doActionWithClusterLock(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(WithDistributedLock::class.java)

        return runBlocking {
            distributedLockProvider.withClusterLock(
                    lockName = annotation.lockName,
                    lockAtLeastFor = ofMillis(annotation.lockAtLeastForMs),
                    lockAtMostFor = ofMillis(annotation.lockAtMostForMs),
                    ifLockedValue = null
            ) {
                pjp.proceed()
            }
        }
    }
}
