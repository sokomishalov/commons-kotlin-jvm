package ru.sokomishalov.commons.cache.redis

import io.lettuce.core.codec.ByteArrayCodec
import io.lettuce.core.codec.RedisCodec
import io.lettuce.core.codec.StringCodec
import java.nio.ByteBuffer
import kotlin.text.Charsets.UTF_8

/**
 * @author sokomishalov
 */
internal class StringByteArrayCodec(
        private val keyCodec: RedisCodec<String, String> = StringCodec(UTF_8),
        private val valueCodec: RedisCodec<ByteArray, ByteArray> = ByteArrayCodec()
) : RedisCodec<String, ByteArray> {
    override fun encodeKey(key: String?): ByteBuffer = keyCodec.encodeKey(key)
    override fun decodeKey(bytes: ByteBuffer?): String = keyCodec.decodeKey(bytes)
    override fun encodeValue(value: ByteArray?): ByteBuffer = valueCodec.encodeValue(value)
    override fun decodeValue(bytes: ByteBuffer?): ByteArray = valueCodec.decodeValue(bytes)
}