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
