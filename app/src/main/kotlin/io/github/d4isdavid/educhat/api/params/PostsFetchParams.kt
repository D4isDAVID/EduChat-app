package io.github.d4isdavid.educhat.api.params

data class PostsFetchParams(
    val limit: Int? = null,
    val after: Int? = null,
    val title: String? = null,
    val categoryId: Int? = null,
)

fun PostsFetchParams.toQuery(): Map<String, String?> {
    return mapOf(
        "limit" to limit?.toString(),
        "after" to after?.toString(),
        "title" to title,
        "categoryId" to categoryId?.toString(),
    )
}
