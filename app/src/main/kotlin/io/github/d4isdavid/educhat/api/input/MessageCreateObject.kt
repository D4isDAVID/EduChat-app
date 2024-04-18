package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class MessageCreateObject(
    val content: String,
)

fun MessageCreateObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
