package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class CategoryCreateObject(
    val name: String,
    val description: String?,
    val parentId: Int?,
)

fun CategoryCreateObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("description", description)
    .put("parentId", parentId)
