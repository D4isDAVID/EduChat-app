package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.cache.APIObjectWithId
import io.github.d4isdavid.educhat.api.utils.getInstant
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.api.utils.nullableInstant
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

data class MessageObject(
    override val id: Int,
    val content: String,
    val createdAt: Instant,
    val editedAt: Instant?,
    val pinned: Boolean,
    val hidden: Boolean,
) : APIObjectWithId

fun createMessageObject(obj: JSONObject) = MessageObject(
    obj.getInt("id"),
    obj.getString("content"),
    obj.getInstant("createdAt"),
    obj.nullableInstant("editedAt"),
    obj.getBoolean("pinned"),
    obj.getBoolean("hidden"),
)

fun createMessagesList(arr: JSONArray) = arr.getJSONObjects().map { createMessageObject(it) }
