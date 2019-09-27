package ru.sokomishalov.commons.spring.net

import org.springframework.util.SocketUtils.*
import ru.sokomishalov.commons.spring.net.SocketType.TCP
import ru.sokomishalov.commons.spring.net.SocketType.UDP

fun randomFreePort(
        type: SocketType = TCP,
        range: IntRange = (PORT_RANGE_MIN..PORT_RANGE_MAX)
): Int {
    return when (type) {
        TCP -> findAvailableTcpPort(range.first, range.last)
        UDP -> findAvailableUdpPort(range.first, range.last)
    }
}