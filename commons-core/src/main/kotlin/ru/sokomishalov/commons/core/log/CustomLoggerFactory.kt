package ru.sokomishalov.commons.core.log

import io.github.classgraph.ClassGraph
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap

/**
 * @author sokomishalov
 */
object CustomLoggerFactory {

    private val loggersMap: MutableMap<String, Logger> = ConcurrentHashMap()

    init {
        ClassGraph()
                .enableClassInfo()
                .disableJarScanning()
                .scan()
                .getClassesImplementing(Loggable::class.java.name)
                .names
                .forEach { loggersMap[it] = loggerFor(it) }

    }

    fun <T : Loggable> getLogger(clazz: Class<T>): Logger {
        val logger = loggersMap[clazz.name]
        return when {
            logger != null -> logger
            else -> {
                val newLogger = loggerFor(clazz)
                loggersMap[clazz.name] = newLogger
                newLogger
            }
        }
    }
}