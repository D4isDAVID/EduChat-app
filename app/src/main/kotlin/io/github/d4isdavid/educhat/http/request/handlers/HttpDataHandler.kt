package io.github.d4isdavid.educhat.http.request.handlers

import java.net.HttpURLConnection

fun HttpURLConnection.handleData(): String {
    return if (responseCode < 400)
        inputStream.bufferedReader().use { it.readText() }
    else
        errorStream.bufferedReader().use { it.readText() }
}
