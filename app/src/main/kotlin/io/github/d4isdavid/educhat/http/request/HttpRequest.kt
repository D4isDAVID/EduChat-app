package io.github.d4isdavid.educhat.http.request

import java.net.HttpURLConnection
import java.net.URL

fun <T> makeHttpRequest(url: URL, hook: HttpURLConnection.() -> T): T {
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.setRequestProperty("User-Agent", System.getProperty("http.agent"))
        return connection.hook()
    } finally {
        connection.disconnect()
    }
}
