package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminPostEditObject(
    val message: AdminMessageEditObject?,
    val title: String?,
    val locked: Boolean?,
    val question: Boolean?,
)

fun AdminPostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
    .put("locked", locked)
    .put("question", question)
