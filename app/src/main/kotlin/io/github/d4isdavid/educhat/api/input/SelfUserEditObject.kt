package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class SelfUserEditObject(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
)

fun SelfUserEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("email", email)
    .put("password", password)
