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