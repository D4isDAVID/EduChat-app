package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class NotificationEditObject(
    val read: Boolean? = null,
)

fun NotificationEditObject.toJSON(): JSONObject = JSONObject()
    .put("read", read)
