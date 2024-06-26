package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class PostCreateObject(
    val message: MessageCreateObject,
    val title: String,
    val question: Boolean,
    val categoryId: Int,
)

fun PostCreateObject.toJSON(): JSONObject = JSONObject()
    .put("message", message.toJSON())
    .put("title", title)
    .put("question", question)
    .put("categoryId", categoryId)
