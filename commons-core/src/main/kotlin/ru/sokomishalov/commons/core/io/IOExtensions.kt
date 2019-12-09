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

package ru.sokomishalov.commons.core.io

import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.Int.Companion.MAX_VALUE

/**
 * @author sokomishalov
 */

fun InputStream.toByteArray(): ByteArray = readBytes()

fun ZipInputStream.toIterableEntries(): Iterable<ZipEntry> = object : Iterable<ZipEntry> {
    override fun iterator(): Iterator<ZipEntry> = object : Iterator<ZipEntry> {
        var next: ZipEntry? = null

        override operator fun hasNext(): Boolean {
            next = nextEntry
            return next != null
        }

        override operator fun next(): ZipEntry = next ?: throw NoSuchElementException()
    }
}

fun File.listFilesDeep(filter: (File) -> Boolean = { true }): List<File> {
    return walkTopDown()
            .maxDepth(MAX_VALUE)
            .filter { it.isFile && filter(it) }
            .toList()
}

fun File.isArchive(): Boolean {
    val fileSignature = runCatching { RandomAccessFile(this, "r").use { raf -> raf.readInt() } }.getOrNull()
    return fileSignature in listOf(0x504B0304, 0x504B0506, 0x504B0708)
}