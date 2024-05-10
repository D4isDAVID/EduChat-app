package io.github.d4isdavid.educhat.api.input

import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.putNullable
import org.json.JSONObject

data class CategoryEditObject(
    val name: String?,
    val description: JSONNullable<String>?,
    val pinned: Boolean?,
    val locked: Boolean?,
    val parentId: Int?,
)

fun CategoryEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .putNullable("description", description)
    .put("pinned", pinned)
    .put("locked", locked)
    .put("parentId", parentId)
