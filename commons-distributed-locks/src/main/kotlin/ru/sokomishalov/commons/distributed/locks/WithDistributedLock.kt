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

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*

/**
 * @author sokomishalov
 */

@Target(FUNCTION, PROPERTY_SETTER, PROPERTY_GETTER)
@Retention(RUNTIME)
annotation class WithDistributedLock(

        // name of lock
        val lockName: String = "lock",

        // will hold the lock for this duration, at a minimum
        val lockAtLeastForMs: Long = 0,

        // will hold the lock for this duration, at a maximum
        val lockAtMostForMs: Long = 0
)