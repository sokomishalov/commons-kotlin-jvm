@file:Suppress("unused", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package ru.sokomishalov.commons.core.images

import ru.sokomishalov.commons.core.http.REACTIVE_NETTY_HTTP_CLIENT
import ru.sokomishalov.commons.core.reactor.awaitStrict
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO


fun ByteArray.toBufferedImage(): BufferedImage {
    return ByteArrayInputStream(this).use {
        ImageIO.read(it)
    }
}

suspend fun getImageByteArray(url: String?, orElse: ByteArray = ByteArray(0)): ByteArray {
    return runCatching {
        REACTIVE_NETTY_HTTP_CLIENT
                .get()
                .uri(url)
                .responseContent()
                .aggregate()
                .asByteArray()
                .awaitStrict()
    }.getOrElse {
        orElse
    }
}

suspend fun getImageDimensions(url: String?, default: Pair<Int, Int> = 1 to 1): Pair<Int, Int> {
    return runCatching {
        val imageByteArray = getImageByteArray(url)
        imageByteArray.toBufferedImage().run { width to height }
    }.getOrElse {
        default
    }
}

suspend fun getImageAspectRatio(url: String?): Double {
    return getImageDimensions(url).run { first.toDouble().div(second) }
}

suspend fun checkImageUrl(url: String?): Boolean {
    return runCatching {
        getImageByteArray(url).toBufferedImage()
        true
    }.getOrElse {
        false
    }
}