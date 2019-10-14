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

import org.jsoup.Jsoup.clean
import org.jsoup.Jsoup.parse
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.safety.Whitelist
import ru.sokomishalov.commons.core.http.REACTIVE_NETTY_HTTP_CLIENT
import ru.sokomishalov.commons.core.reactor.awaitStrict
import java.nio.charset.StandardCharsets.UTF_8


/**
 * @author sokomishalov
 */
suspend fun getWebPage(url: String): Document {
    val document = REACTIVE_NETTY_HTTP_CLIENT
            .get()
            .uri(url)
            .responseContent()
            .aggregate()
            .asString(UTF_8)
            .awaitStrict()

    return parse(document)
}

fun Element.getSingleElementByClass(name: String): Element {
    return getElementsByClass(name).first()
}

fun Element.getSingleElementByTag(name: String): Element {
    return getElementsByTag(name).first()
}

fun Element.getImageBackgroundUrl(): String {
    val style = attr("style")
    return style.substring(style.indexOf("http"), style.indexOf(")"))
}

fun Element.fixText(): String {
    return clean(toString(), Whitelist.none())
}