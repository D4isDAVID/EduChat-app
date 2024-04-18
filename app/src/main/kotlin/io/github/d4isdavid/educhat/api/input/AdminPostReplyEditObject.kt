package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminPostReplyEditObject(
    val content: String?,
    val hidden: Boolean?,
    val pinned: Boolean?,
)

fun AdminPostReplyEditObject.toJSON(): JSONObject = JSONObject()
    .put("content", content)
    .put("hidden", hidden)
    .put("pinned", pinned)
