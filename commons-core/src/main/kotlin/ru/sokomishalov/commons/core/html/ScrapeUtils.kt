@file:Suppress("unused")

package ru.sokomishalov.commons.core.html

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.Jsoup.parse
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


/**
 * @author sokomishalov
 */

suspend fun getWebPage(url: String): Document = withContext(IO) {
    Jsoup.connect(url).get()
}

fun Element.getSingleElementByClass(name: String): Element {
    return getElementsByClass(name).first()
}

fun Element.getImageBackgroundUrl(): String {
    return attr("style")
            .run { substring(indexOf("http"), indexOf(")")) }
}

suspend fun Element.fixText(): String {
    val titleDoc = withContext(IO) {
        parse(html())
    }

    val allAnchors = titleDoc.select("a")
    val twitterAnchors = titleDoc.select("a[href^=/]")
    val unwantedAnchors = ArrayList<Element>()

    allAnchors.filterNotTo(unwantedAnchors) { twitterAnchors.contains(it) }
    unwantedAnchors.forEach { it.remove() }

    return titleDoc.text()
}
