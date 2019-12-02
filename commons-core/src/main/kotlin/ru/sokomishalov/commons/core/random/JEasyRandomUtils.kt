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

package ru.sokomishalov.commons.core.random

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import java.nio.charset.StandardCharsets.UTF_8

/**
 * @author sokomishalov
 */
val EASY_RANDOM_PARAMS: EasyRandomParameters = EasyRandomParameters()
        .seed((0L..1000L).random())
        .objectPoolSize(100)
        .randomizationDepth(3)
        .charset(UTF_8)
        .stringLengthRange(5, 20)
        .collectionSizeRange(0, 10)
        .scanClasspathForConcreteTypes(true)
        .overrideDefaultInitialization(false)
        .ignoreRandomizationErrors(true)


inline fun <reified T> randomPojo(params: EasyRandomParameters = EASY_RANDOM_PARAMS): T {
    return EasyRandom(params).nextObject(T::class.java)
}

inline fun <reified T> randomPojoSequence(params: EasyRandomParameters = EASY_RANDOM_PARAMS) = sequence<T> {
    while (true) {
        yield(randomPojo(params))
    }
}