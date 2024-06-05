package io.github.d4isdavid.educhat.api.params

data class CategoryPostsFetchParams(
    val limit: Int? = null,
    val after: Int? = null,
    val before: Int? = null,
)

fun CategoryPostsFetchParams.toQuery(): Map<String, String?> {
    return mapOf(
        "limit" to limit?.toString(),
        "after" to after?.toString(),
        "before" to before?.toString(),
    )
}
