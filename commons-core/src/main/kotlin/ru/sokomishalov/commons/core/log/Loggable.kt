@file:Suppress("JAVA_CLASS_ON_COMPANION", "unused")

package ru.sokomishalov.commons.core.log

interface Loggable {

    fun log(s: String?) = logInfo(s)

    fun logInfo(s: String?) = logger().info(s)

    fun logDebug(s: String?) = logger().debug(s)

    fun logWarn(t: Throwable) = logWarn(t.message)

    fun logWarn(message: String?) = logger().warn(message)

    fun logError(t: Throwable) = logError(t.message, t)

    fun logError(message: String?, t: Throwable) = logger().error(message, t)

}
