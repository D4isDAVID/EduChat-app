package io.github.d4isdavid.educhat.api.utils

import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

fun JSONObject.getInstant(name: String): Instant = Instant.parse(getString(name))

fun JSONObject.nullableString(name: String) = if (isNull(name)) null else getString(name)
fun JSONObject.nullableInt(name: String) = if (isNull(name)) null else getInt(name)
fun JSONObject.nullableBoolean(name: String) = if (isNull(name)) null else getBoolean(name)
fun JSONObject.nullableInstant(name: String) = if (isNull(name)) null else getInstant(name)

fun JSONArray.getJSONObjects() = Array(length()) { getJSONObject(it) }

data class JSONNullable<T>(
    val value: T?,
)

fun JSONObject.putNullable(name: String, value: JSONNullable<*>?): JSONObject =
    put(name, value?.let { it.value ?: JSONObject.NULL })
