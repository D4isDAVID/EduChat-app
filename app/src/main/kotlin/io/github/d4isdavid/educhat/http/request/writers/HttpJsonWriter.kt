package io.github.d4isdavid.educhat.http.request.writers

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection

fun HttpURLConnection.writeJsonObject(data: JSONObject) {
    setRequestProperty("Content-Type", "application/json")
    writeData(data.toString())
}

@Suppress("unused")
fun HttpURLConnection.writeJsonArray(data: JSONArray) {
    setRequestProperty("Content-Type", "application/json")
    writeData(data.toString())
}
