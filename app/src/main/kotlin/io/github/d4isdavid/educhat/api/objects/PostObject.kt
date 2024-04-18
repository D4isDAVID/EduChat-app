package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import org.json.JSONArray
import org.json.JSONObject

data class PostObject(
    val message: MessageObject,
    val title: String,
    val pinned: Boolean,
    val locked: Boolean,
    val categoryId: Int,
)

fun createPostObject(obj: JSONObject) = PostObject(
    createMessageObject(obj.getJSONObject("message")),
    obj.getString("title"),
    obj.getBoolean("pinned"),
    obj.getBoolean("locked"),
    obj.getInt("categoryId"),
)

fun createPostsArray(arr: JSONArray) = arr.getJSONObjects().map { createPostObject(it) }
