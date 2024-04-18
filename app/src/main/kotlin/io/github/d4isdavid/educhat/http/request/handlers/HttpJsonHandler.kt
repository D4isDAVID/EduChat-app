package io.github.d4isdavid.educhat.http.request.handlers

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection

fun HttpURLConnection.handleJsonObject(): JSONObject {
    return JSONObject(handleData())
}

fun HttpURLConnection.handleJsonArray(): JSONArray {
    return JSONArray(handleData())
}
