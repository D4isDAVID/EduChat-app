package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class UserEditObject(
    val name: String?,
    val email: String?,
    val password: String?,
)

fun UserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("email", email)
    .put("password", password)
