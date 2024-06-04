package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class AdminUserEditObject(
    val name: String? = null,
    val admin: Boolean? = null,
    val helper: Boolean? = null,
)

fun AdminUserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("admin", admin)
    .put("helper", helper)
