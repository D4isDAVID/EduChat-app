package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminMessageEditObject(
    val content: String?,
    val hidden: Boolean?,
)

fun AdminMessageEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
    .put("hidden", hidden)
