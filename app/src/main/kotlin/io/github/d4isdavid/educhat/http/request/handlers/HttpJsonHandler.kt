package io.github.d4isdavid.educhat.http.request.handlers

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

fun HttpURLConnection.handleJsonObject(): JSONObject? {
    return try {
        JSONObject(handleData())
    } catch (e: JSONException) {
        Log.e("HttpJsonHandler", e.stackTraceToString())
        return null
    }
}

fun HttpURLConnection.handleJsonArray(): JSONArray? {
    return try {
        JSONArray(handleData())
    } catch (e: JSONException) {
        Log.e("HttpJsonHandler", e.stackTraceToString())
        return null
    }
}
