package io.github.d4isdavid.educhat.api.input

import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.putNullable
import org.json.JSONObject

data class PostEditObject(
    val message: MessageEditObject? = null,
    val title: String? = null,
    val question: Boolean? = null,
    val answerId: JSONNullable<Int>? = null,
)

fun PostEditObject.toJSON(): JSONObject = JSONObject()
    .put("message", message?.toJSON())
    .put("title", title)
    .put("question", question)
    .putNullable("answerId", answerId)
