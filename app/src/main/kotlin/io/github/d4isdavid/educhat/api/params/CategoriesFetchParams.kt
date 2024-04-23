package io.github.d4isdavid.educhat.api.params

data class CategoriesFetchParams(
    val parentId: Int?,
)

fun CategoriesFetchParams.toQuery(): Map<String, String?> {
    return mapOf("parentId" to parentId?.toString())
}
