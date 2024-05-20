package io.github.d4isdavid.educhat.http.request

import java.net.HttpURLConnection
import java.net.URL

const val CONNECT_TIMEOUT_MILLIS = 15_000
const val READ_TIMEOUT_MILLIS = 60_000

fun <T> makeHttpRequest(url: URL, hook: HttpURLConnection.() -> T): T {
    val connection = url.openConnection() as HttpURLConnection

    connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
    connection.readTimeout = READ_TIMEOUT_MILLIS

    try {
        connection.setRequestProperty("User-Agent", System.getProperty("http.agent"))
        return hook(connection)
    } finally {
        connection.disconnect()
    }
}
