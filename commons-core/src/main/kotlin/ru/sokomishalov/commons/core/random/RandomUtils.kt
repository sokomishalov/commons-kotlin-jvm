@file:Suppress("unused")

package ru.sokomishalov.commons.core.random

import org.apache.commons.lang3.RandomStringUtils.random
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

fun randomString(
        length: Int = 20,
        range: IntRange = (length..length),
        useLetters: Boolean = true,
        useDigits: Boolean = false
): String = random(range.count(), range.first, range.last, useLetters, useDigits)