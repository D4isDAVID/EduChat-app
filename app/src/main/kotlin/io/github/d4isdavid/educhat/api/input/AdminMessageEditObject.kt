package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminMessageEditObject(
    val content: String?,
    val pinned: Boolean?,
)

fun AdminMessageEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
    .put("pinned", pinned)
