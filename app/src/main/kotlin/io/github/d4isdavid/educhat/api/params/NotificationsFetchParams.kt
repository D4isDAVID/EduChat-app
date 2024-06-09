package io.github.d4isdavid.educhat.api.params

data class NotificationsFetchParams(
    val onlyNew: Boolean? = null,
    val onlyUnread: Boolean? = null,
)

fun NotificationsFetchParams.toQuery(): Map<String, String?> {
    return mapOf(
        "onlyNew" to onlyNew?.let { if (it) "1" else "0" },
        "onlyUnread" to onlyUnread?.let { if (it) "1" else "0" },
    )
}
