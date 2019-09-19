@file:Suppress("unused")

package ru.sokomishalov.commons.core.io

import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import org.apache.commons.io.IOUtils.toByteArray as iOUtilsToByteArray

/**
 * @author sokomishalov
 */

fun InputStream.toByteArray(): ByteArray = iOUtilsToByteArray(this)

fun ZipInputStream.toIterableEntries(): Iterable<ZipEntry> = object : Iterable<ZipEntry> {
    override fun iterator(): Iterator<ZipEntry> {
        return object : Iterator<ZipEntry> {
            var next: ZipEntry? = nextEntry

            override operator fun hasNext() = next != null
            override operator fun next(): ZipEntry {
                val tmp = next ?: throw NoSuchElementException()
                next = nextEntry
                return tmp
            }
        }
    }
}