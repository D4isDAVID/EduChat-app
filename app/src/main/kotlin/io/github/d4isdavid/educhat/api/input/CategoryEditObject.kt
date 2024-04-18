package io.github.d4isdavid.educhat.api.input

import org.json.JSONObject

data class CategoryEditObject(
    val name: String?,
    val description: String?,
    val pinned: Boolean?,
    val locked: Boolean?,
    val parentId: Int?,
)

fun CategoryEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .put("description", description)
    .put("pinned", pinned)
    .put("locked", locked)
    .put("parentId", parentId)
