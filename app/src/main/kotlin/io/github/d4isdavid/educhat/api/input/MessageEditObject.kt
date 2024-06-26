package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class MessageEditObject(
    val content: String? = null,
)

fun MessageEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
