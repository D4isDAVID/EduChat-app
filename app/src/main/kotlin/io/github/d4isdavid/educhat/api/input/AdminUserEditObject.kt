package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminUserEditObject(
    val name: String?,
    val admin: Boolean?,
    val helper: Boolean?,
)

fun AdminUserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("admin", admin)
    .put("helper", helper)
