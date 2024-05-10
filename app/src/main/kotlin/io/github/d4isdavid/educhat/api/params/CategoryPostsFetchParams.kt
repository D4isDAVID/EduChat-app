package io.github.d4isdavid.educhat.api.params

data class CategoryPostsFetchParams(
    val limit: Int?,
    val after: Int?,
    val before: Int?,
)

fun CategoryPostsFetchParams.toQuery(): Map<String, String?> {
    return mapOf(
        "limit" to limit?.toString(),
        "after" to after?.toString(),
        "before" to before?.toString(),
    )
}
