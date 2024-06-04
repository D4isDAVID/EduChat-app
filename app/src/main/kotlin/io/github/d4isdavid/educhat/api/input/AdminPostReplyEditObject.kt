package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminPostReplyEditObject(
    val content: String? = null,
    val pinned: Boolean? = null,
)

fun AdminPostReplyEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
    .put("pinned", pinned)
