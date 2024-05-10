package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminUserEditObject(
    val name: String?,
)

fun AdminUserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
