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
package ru.sokomishalov.commons.distributed.locks

import kotlinx.coroutines.runBlocking
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.time.Duration.ofMillis

@Aspect
class WithDistributedLockAspect(private val distributedLockProvider: DistributedLockProvider) {

    @Around("@annotation(ru.sokomishalov.commons.distributed.locks.WithDistributedLock) && execution(* *.*(..))")
    fun doActionWithDistributedLock(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val annotation = signature.method.getAnnotation(WithDistributedLock::class.java)

        return runBlocking {
            distributedLockProvider.withDistributedLock(
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
