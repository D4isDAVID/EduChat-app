package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class SelfUserEditObject(
    val name: String?,
    val email: String?,
    val password: String?,
)

fun SelfUserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("email", email)
    .put("password", password)
