package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class PostReplyEditObject(
    val content: String? = null,
)

fun PostReplyEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content ?: JSONObject.NULL)
