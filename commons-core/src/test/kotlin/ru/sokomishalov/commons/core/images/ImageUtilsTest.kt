package ru.sokomishalov.commons.core.images

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.abs

class ImageUtilsTest {

    private val imageWidth = 200
    private val imageHeight = 300
    private val imageUrl = "https://picsum.photos/$imageWidth/$imageHeight"
    private val invalidImageUrl = "https://lol.kek/cheburek"
    private val imageUrl401 = "https://httpstat.us/401"
    private val imageUrl404 = "https://httpstat.us/404"


    @Test
    fun `Get random image by url and check dimensions`() {
        val imageDimensions = runBlocking { getImageDimensions(imageUrl) }

        assertEquals(imageWidth, imageDimensions.first)
        assertEquals(imageHeight, imageDimensions.second)
    }

    @Test
    fun `Get random image by url and check aspect ratio`() {
        val expected = imageWidth.toDouble().div(imageHeight)
        val result = runBlocking { getImageAspectRatio(imageUrl) }

        assertTrue(abs(expected - result) < 0.01)
    }

    @Test
    fun `Check image availability`() {
        runBlocking {
            assertTrue(checkImageUrl(imageUrl))
            assertFalse(checkImageUrl(invalidImageUrl))
            assertFalse(checkImageUrl(imageUrl401))
            assertFalse(checkImageUrl(imageUrl404))
        }
    }
}