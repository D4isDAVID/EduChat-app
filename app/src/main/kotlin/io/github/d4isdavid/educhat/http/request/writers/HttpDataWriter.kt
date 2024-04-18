package io.github.d4isdavid.educhat.http.request.writers

import java.net.HttpURLConnection

fun HttpURLConnection.writeData(data: ByteArray) {
    doOutput = true
    outputStream.write(data)
}
