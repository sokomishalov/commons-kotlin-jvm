@file:Suppress("unused")

package ru.sokomishalov.commons.core.images

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

suspend fun getImageByteArray(url: String?, orElse: ByteArray = ByteArray(0)): ByteArray = runCatching {
    withContext(IO) {
        val bufferedImage = ImageIO.read(URL(url))
        ByteArrayOutputStream().use {
            ImageIO.write(bufferedImage, "jpg", it)
            it.toByteArray()
        }
    }
}.getOrElse {
    orElse
}

suspend fun getImageDimensions(url: String?, default: Pair<Int, Int> = 1 to 1): Pair<Int, Int> = runCatching {
    withContext(IO) {
        ImageIO.read(URL(url)).run { width to height }
    }
}.getOrElse {
    default
}