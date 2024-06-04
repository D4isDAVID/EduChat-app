package io.github.d4isdavid.educhat.api.input

import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.putNullable
import org.json.JSONObject

data class CategoryEditObject(
    val name: String? = null,
    val description: JSONNullable<String>? = null,
    val pinned: Boolean? = null,
    val locked: Boolean? = null,
    val parentId: Int? = null,
)

fun CategoryEditObject.toJSON(): JSONObject = JSONObject()
    .put("name", name)
    .putNullable("description", description)
    .put("pinned", pinned)
    .put("locked", locked)
    .put("parentId", parentId)
