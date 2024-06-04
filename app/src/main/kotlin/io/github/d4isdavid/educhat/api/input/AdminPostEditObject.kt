package io.github.d4isdavid.educhat.api.input

import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.putNullable
import org.json.JSONObject

data class AdminPostEditObject(
    val message: AdminMessageEditObject?,
    val title: String?,
    val question: Boolean?,
    val answerId: JSONNullable<Int>?,
    val locked: Boolean?,
)

fun AdminPostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
    .put("question", question)
    .putNullable("answerId", answerId)
    .put("locked", locked)
