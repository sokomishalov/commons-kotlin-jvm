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
package ru.sokomishalov.commons.cache.util

import ru.sokomishalov.commons.core.random.randomString
import java.lang.System.currentTimeMillis

data class DummyModel(
        val id: Long = 0,
        val name: String? = randomString(10),
        val createdAt: Long = currentTimeMillis()
) : Comparable<DummyModel> {

    override fun compareTo(other: DummyModel): Int = id.compareTo(other.id)
}
