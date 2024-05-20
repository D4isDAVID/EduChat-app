package io.github.d4isdavid.educhat.api.input

import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.putNullable
import org.json.JSONObject

data class PostEditObject(
    val message: MessageCreateObject?,
    val title: String?,
    val question: Boolean?,
    val answerId: JSONNullable<Int>?,
)

fun PostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
    .put("question", question)
    .putNullable("answerId", answerId)
