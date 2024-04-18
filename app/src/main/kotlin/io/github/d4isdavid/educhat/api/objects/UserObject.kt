package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

data class UserObject(
    val id: Int,
    val name: String,
    val createdAt: Instant,
    val admin: Boolean,
    val student: Boolean,
    val teacher: Boolean,
)

fun createUserObject(obj: JSONObject) = UserObject(
    obj.getInt("id"),
    obj.getString("name"),
    obj.getInstant("createdAt"),
    obj.getBoolean("admin"),
    obj.getBoolean("student"),
    obj.getBoolean("teacher"),
)

fun createUsersArray(arr: JSONArray) = arr.getJSONObjects().map { createUserObject(it) }
