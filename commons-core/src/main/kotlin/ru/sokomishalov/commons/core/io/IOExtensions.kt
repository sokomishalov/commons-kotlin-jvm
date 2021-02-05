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
@file:Suppress("unused")

package ru.sokomishalov.commons.core.io

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.Int.Companion.MAX_VALUE

/**
 * @author sokomishalov
 */

fun InputStream.toByteArray(): ByteArray = readBytes()

fun ZipInputStream.toIterableEntries(): Iterable<ZipEntry> = ZipIterable(this)

fun File.zipFiles(filesMap: Map<String, ByteArray>) {
    outputStream().use { fos ->
        ZipOutputStream(fos).use { zos ->
            filesMap.forEach { (name, bytes) ->
                val file = File(name)
                val entry = ZipEntry(file.name)
                zos.putNextEntry(entry)
                zos.write(bytes)
            }
        }
    }
}

fun File.unzipTo(folder: File) {
    if (folder.exists().not()) folder.mkdirs()

    FileInputStream(this).use { fis ->
        ZipInputStream(fis).use { zis ->
            zis.toIterableEntries().forEach { ze ->
                val newFile = File("${folder.path}/${ze.name}")
                when {
                    ze.isDirectory -> newFile.mkdirs()
                    else -> {
                        newFile.parentFile.mkdir()
                        FileOutputStream(newFile).use { fos ->
                            zis.copyTo(fos)
                        }
                    }
                }
            }
        }
    }
}

fun File.unzipTo(path: String) {
    unzipTo(File(path))
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