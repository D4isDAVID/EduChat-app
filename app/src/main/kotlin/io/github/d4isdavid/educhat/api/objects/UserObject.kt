package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.cache.APIObjectWithId
import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

data class UserObject(
    override val id: Int,
    val name: String,
    val createdAt: Instant,
    val admin: Boolean,
    val student: Boolean,
    val teacher: Boolean,
) : APIObjectWithId

fun createUserObject(obj: JSONObject) = UserObject(
    obj.getInt("id"),
    obj.getString("name"),
    obj.getInstant("createdAt"),
    obj.getBoolean("admin"),
    obj.getBoolean("student"),
    obj.getBoolean("teacher"),
)

fun createUsersList(arr: JSONArray) = arr.getJSONObjects().map { createUserObject(it) }
