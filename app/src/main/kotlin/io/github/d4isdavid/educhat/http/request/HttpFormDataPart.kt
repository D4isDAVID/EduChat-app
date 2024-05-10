package io.github.d4isdavid.educhat.http.request

data class HttpFormDataPart(
    val content: String,
    val name: String,
    val contentType: String? = null,
    val filename: String? = null
)
