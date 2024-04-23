package io.github.d4isdavid.educhat.api.objects

import io.github.d4isdavid.educhat.api.cache.APIObjectWithId
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.api.utils.nullableInt
import io.github.d4isdavid.educhat.api.utils.nullableString
import org.json.JSONArray
import org.json.JSONObject

data class CategoryObject(
    override val id: Int,
    val name: String,
    val description: String?,
    val pinned: Boolean,
    val locked: Boolean,
    val parentId: Int?,
) : APIObjectWithId

fun createCategoryObject(obj: JSONObject) = CategoryObject(
    obj.getInt("id"),
    obj.getString("name"),
    obj.nullableString("description"),
    obj.getBoolean("pinned"),
    obj.getBoolean("locked"),
    obj.nullableInt("parentId"),
)

fun createCategoriesList(arr: JSONArray) = arr.getJSONObjects().map { createCategoryObject(it) }
