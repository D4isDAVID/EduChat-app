package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class CategoryCreateObject(
    val name: String,
    val description: String? = null,
    val parentId: Int? = null,
)

fun CategoryCreateObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("description", description)
    .put("parentId", parentId)
