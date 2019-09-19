package ru.sokomishalov.commons.core.log

import io.github.classgraph.ClassGraph
import org.slf4j.Logger

/**
 * @author sokomishalov
 */
object CustomLoggerFactory {

    private val loggersMap: MutableMap<String, Logger> = mutableMapOf()

    init {
        ClassGraph()
                .enableClassInfo()
                .disableJarScanning()
                .scan()
                .getClassesImplementing(Loggable::class.java.name)
                .names
                .forEach { loggersMap[it] = loggerFor(it) }

    }

    fun <T : Loggable> getLogger(clazz: Class<T>): Logger = loggersMap[clazz.name] ?: loggerFor(clazz)
}