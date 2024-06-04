package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class UserCreateObject(
    val name: String,
    val email: String,
    val password: String,
    val student: Boolean? = null,
    val teacher: Boolean? = null,
)

fun UserCreateObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("email", email)
    .put("password", password)
    .put("student", student)
    .put("teacher", teacher)
