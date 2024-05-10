package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminPostEditObject(
    val message: MessageEditObject?,
    val title: String?,
    val pinned: Boolean?,
    val locked: Boolean?,
)

fun AdminPostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
    .put("pinned", pinned)
    .put("locked", locked)
