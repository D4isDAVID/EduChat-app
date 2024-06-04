package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminMessageEditObject(
    val content: String? = null,
    val pinned: Boolean? = null,
)

fun AdminMessageEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
    .put("pinned", pinned)
