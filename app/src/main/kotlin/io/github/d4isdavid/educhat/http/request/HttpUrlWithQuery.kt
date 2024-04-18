package io.github.d4isdavid.educhat.http.request

import java.net.URL
import java.net.URLEncoder

fun createUrlWithQuery(baseUrl: String, queryParams: Map<String, String>): URL {
    val query = queryParams
        .map { (k, v) -> "${URLEncoder.encode(k, "utf-8")}=${URLEncoder.encode(v, "utf-8")}" }
        .joinToString("&")

    return URL("$baseUrl?$query")
}
