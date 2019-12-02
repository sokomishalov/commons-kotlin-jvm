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

@file:Suppress("unused")

package ru.sokomishalov.commons.spring.cache.caffeine

import com.github.benmanes.caffeine.cache.Caffeine.newBuilder
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import java.time.Duration

/**
 * @author sokomishalov
 */

fun createDefaultCaffeineCacheManager(
        cacheNames: List<String>,
        expireAfterWrite: Duration? = null,
        expireAfterAccess: Duration? = null
): CacheManager {
    return CaffeineCacheManager().apply {
        setCacheNames(cacheNames)
        setCaffeine(newBuilder().apply {
            when {
                expireAfterWrite != null -> expireAfterWrite(expireAfterWrite)
                expireAfterAccess != null -> expireAfterAccess(expireAfterAccess)
            }
        })
    }
}