package ru.sokomishalov.commons.core.log

import org.junit.Test

/**
 * @author sokomishalov
 */

class LogUtilsTest {

    @Test
    fun `Assert logger`() {
        LogWithInterface().doJob()
        LogWithDelegate().doJob()
    }

}

class LogWithInterface : Loggable {
    fun doJob() {
        log("test")
    }
}

class LogWithDelegate {
    private val log by loggerDelegate()
    fun doJob() {
        log.info("test")
    }
}