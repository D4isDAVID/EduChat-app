package io.github.d4isdavid.educhat.api.utils

import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

fun JSONObject.getInstant(name: String) = Instant.parse(getString(name))

fun JSONObject.nullableString(name: String) = if (isNull(name)) null else getString(name)
fun JSONObject.nullableInt(name: String) = if (isNull(name)) null else getInt(name)
fun JSONObject.nullableInstant(name: String) = if (isNull(name)) null else getInstant(name)

fun JSONArray.getJSONObjects() = Array(length()) { getJSONObject(it) }
