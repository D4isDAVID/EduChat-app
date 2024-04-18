package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class PostEditObject(
    val message: MessageCreateObject?,
    val title: String?,
)

fun PostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
