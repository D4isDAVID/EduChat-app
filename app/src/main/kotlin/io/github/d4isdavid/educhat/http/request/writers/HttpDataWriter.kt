package io.github.d4isdavid.educhat.http.request.writers

import java.net.HttpURLConnection

fun HttpURLConnection.writeData(data: String) {
    doOutput = true
    outputStream.write(data.toByteArray())
}
