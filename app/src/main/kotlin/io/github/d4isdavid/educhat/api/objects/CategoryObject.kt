package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.api.utils.nullableInt
import io.github.d4isdavid.educhat.api.utils.nullableString
import org.json.JSONArray
import org.json.JSONObject

data class CategoryObject(
    val id: Int,
    val name: String,
    val description: String?,
    val pinned: Boolean,
    val locked: Boolean,
    val parentId: Int?,
)

fun createCategoryObject(obj: JSONObject) = CategoryObject(
    obj.getInt("id"),
    obj.getString("name"),
    obj.nullableString("description"),
    obj.getBoolean("pinned"),
    obj.getBoolean("locked"),
    obj.nullableInt("parentId"),
)

fun createCategoriesArray(arr: JSONArray) = arr.getJSONObjects().map { createCategoryObject(it) }
