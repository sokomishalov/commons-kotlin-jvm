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
    override fun iterator(): Iterator<ZipEntry> = object : Iterator<ZipEntry> {
        var next: ZipEntry? = null

        override operator fun hasNext(): Boolean {
            next = nextEntry
            return next != null
        }

        override operator fun next(): ZipEntry {
            return next ?: throw NoSuchElementException()
        }
    }
}