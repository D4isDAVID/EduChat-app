package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class PostReplyCreateObject(
    val content: String,
)

fun PostReplyCreateObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
