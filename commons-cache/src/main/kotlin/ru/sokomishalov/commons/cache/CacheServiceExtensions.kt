package ru.sokomishalov.commons.cache

/**
 * @author sokomishalov
 */
suspend inline fun <reified T> CacheService.getOne(key: String): T? = getOne(key, T::class.java)

suspend inline fun <reified T> CacheService.getList(key: String): List<T> = getList(key, T::class.java)

suspend inline fun <reified T> CacheService.getMap(key: String): Map<String, T> = getMap(key, T::class.java)

suspend inline fun <reified T> CacheService.getFromMap(key: String, mapKey: String): T? = getFromMap(key, mapKey, T::class.java)

suspend inline fun <reified T : Any> CacheService.find(glob: String): List<T> = find(glob, T::class.java)