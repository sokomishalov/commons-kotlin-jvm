package ru.sokomishalov.commons.cache

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.sokomishalov.commons.cache.util.DummyModel
import ru.sokomishalov.commons.core.log.Loggable
import ru.sokomishalov.commons.core.random.randomString
import java.util.UUID.randomUUID
import kotlin.Long.Companion.MAX_VALUE
import kotlin.Long.Companion.MIN_VALUE

abstract class AbstractCacheServiceTest {

    companion object : Loggable {
        private const val CACHE_KEY = "key"
        private const val CACHE_VALUE = "value"
    }

    protected abstract val cacheService: CacheService

    @After
    open fun tearDown() {
        runBlocking {
            cacheService.deleteAll()
        }
    }

    @Test
    open fun `Put strings`() {
        runBlocking {
            val data = listOf(
                    "key1" to "value1",
                    "key2" to "value2",
                    "key3" to "value3"
            )
            data.forEach { (key, value) ->
                cacheService.put(key, value)
            }

            data.shuffled().forEach { (key, value) ->
                assertEquals(value, cacheService.getOne<String>(key))
            }
        }
    }

    @Test
    open fun `Put and delete strings`() {
        runBlocking {
            cacheService.put(CACHE_KEY, CACHE_VALUE)
            assertEquals(CACHE_VALUE, cacheService.getOne<String>(CACHE_KEY))

            cacheService.delete(CACHE_KEY)
            assertNull(cacheService.getOne(CACHE_KEY))
        }
    }

    @Test
    open fun `Put and replace strings`() {
        runBlocking {
            cacheService.put(CACHE_KEY, CACHE_VALUE)
            assertEquals(CACHE_VALUE, cacheService.getOne<String>(CACHE_KEY))

            val newValue = "newValue"
            cacheService.put(CACHE_KEY, newValue)
            assertEquals(newValue, cacheService.getOne<String>(CACHE_KEY))
        }
    }

    @Test
    open fun `Put objects`() {
        runBlocking {
            val data = mutableListOf(
                    "key1" to DummyModel(MIN_VALUE, "firstDummy"),
                    "key2" to DummyModel(0, "secondDummy"),
                    "key3" to DummyModel(MAX_VALUE, "thirdDummy")
            )
            data.forEach { (key, value) ->
                cacheService.put(key, value)
            }

            data.shuffled().forEach { (key, value) ->
                assertEquals(value, cacheService.getOne<DummyModel>(key))
            }
        }
    }

    @Test
    open fun `Put and delete objects`() {
        runBlocking {
            val dummy = DummyModel(1, "DummyModel")
            cacheService.put(CACHE_KEY, dummy)
            assertEquals(dummy, cacheService.getOne<DummyModel>(CACHE_KEY))

            cacheService.delete(CACHE_KEY)
            assertNull(cacheService.getOne<DummyModel>(CACHE_KEY))
        }
    }

    @Test
    open fun `Put and replace object`() {
        runBlocking {
            val firstDummy = DummyModel(MIN_VALUE, "firstDummy")
            cacheService.put(CACHE_KEY, firstDummy)
            assertEquals(firstDummy, cacheService.getOne<DummyModel>(CACHE_KEY))

            val secondDummy = DummyModel(MAX_VALUE, "secondDummy")
            cacheService.put(CACHE_KEY, secondDummy)
            assertEquals(secondDummy, cacheService.getOne<DummyModel>(CACHE_KEY))
        }
    }

    @Test
    open fun `Get not existing value`() {
        runBlocking {
            assertNull(cacheService.getOne<String>(randomString(10)))
        }
    }

    @Test
    open fun `Put string list`() {
        runBlocking {
            val data = listOf("value1", "value2", "value3")
            cacheService.put(CACHE_KEY, data)

            val result = cacheService.getList<String>(CACHE_KEY)
            assertEquals(data, result.sorted())
        }
    }

    @Test
    open fun `Put object list`() {
        runBlocking {
            val data = mutableListOf(
                    DummyModel(1, "aFirstDummy"),
                    DummyModel(2, "bSecondDummy"),
                    DummyModel(3, "cThirdDummy")
            )
            cacheService.put(CACHE_KEY, data)

            val result = cacheService.getList<DummyModel>(CACHE_KEY)
            assertEquals(data, result.sorted())
        }
    }

    @Test
    open fun `Put empty list`() {
        runBlocking {
            cacheService.put(CACHE_KEY, emptyList<Any>())
            assertEquals(emptyList<Any>(), cacheService.getList<Any>(CACHE_KEY))
        }
    }

    @Test
    open fun `Get non existing list`() {
        runBlocking {
            assertEquals(emptyList<Any>(), cacheService.getList<Any>(CACHE_KEY))
        }
    }

    @Test
    open fun `Put map`() {
        runBlocking {
            val data = mapOf(
                    "key1" to "value1",
                    "key2" to "value2",
                    "key3" to "value3"
            )
            cacheService.put(CACHE_KEY, data)

            assertEquals(data, cacheService.getMap<String>(CACHE_KEY))
            data.forEach { (key, value) ->
                assertEquals(value, cacheService.getFromMap<String>(CACHE_KEY, key))
            }
        }
    }

    @Test
    open fun `Put empty map`() {
        runBlocking {
            cacheService.put(CACHE_KEY, emptyMap<Any, Any>())
            assertEquals(emptyMap<Any, Any>(), cacheService.getMap<Any>(CACHE_KEY))
        }
    }

    @Test
    open fun `Get non existing map`() {
        runBlocking {
            assertEquals(emptyMap<Any, Any>(), cacheService.getMap<Any>(randomUUID().toString()))
        }
    }

    @Test
    open fun `Get from not existing map`() {
        runBlocking {
            assertNull(cacheService.getFromMap(randomUUID().toString(), randomUUID().toString()))
        }
    }

    @Test
    open fun `Test find by glob pattern`() {
        runBlocking {
            val data = mapOf(
                    "kekpek1" to "kekpek1",
                    "kekpek2" to "kekpek2",
                    "cheburek1" to "cheburek1",
                    "cheburek2" to "cheburek2"
            )
            data.forEach { (k, v) -> cacheService.put(k, v) }

            val all = cacheService.find<String>("*")
            assertEquals(4, all.size)

            val found = cacheService.find<String>("*kek*")
            assertEquals(2, found.size)

            val notFound = cacheService.find<String>("*hehmda")
            assertEquals(0, notFound.size)
        }
    }
}
