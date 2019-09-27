@file:Suppress("unused")

package ru.sokomishalov.commons.core.url

import org.apache.commons.lang3.SystemUtils.getHostName
import ru.sokomishalov.commons.core.consts.EMPTY
import java.net.InetAddress

/**
 * @author sokomishalov
 */
val HOSTNAME: String
    get() = getHostName()
            ?: runCatching { InetAddress.getLocalHost().hostAddress }.getOrNull()
            ?: EMPTY