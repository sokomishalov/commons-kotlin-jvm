/**
 * Copyright (c) 2019-present Mikhael Sokolov
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
@file:Suppress(
        "UNCHECKED_CAST",
        "unused",
        "NOTHING_TO_INLINE"
)

package ru.sokomishalov.commons.spring.cache

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

// until merged
// https://github.com/spring-projects/spring-framework/pull/23927/files

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T : Any> Cache.get(key: Any): T? = get(key, T::class.java)

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline operator fun Cache.get(key: Any): Cache.ValueWrapper? = get(key)

inline operator fun Cache.set(key: Any, value: Any?) = put(key, value)

inline operator fun CacheManager.get(name: String): Cache? = getCache(name)