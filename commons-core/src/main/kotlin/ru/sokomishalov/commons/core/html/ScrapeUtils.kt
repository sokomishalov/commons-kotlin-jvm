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
