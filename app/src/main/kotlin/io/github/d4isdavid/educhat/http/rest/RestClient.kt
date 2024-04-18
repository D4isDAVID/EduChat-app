package io.github.d4isdavid.educhat.http.rest

import io.github.d4isdavid.educhat.http.request.createUrlWithQuery
import io.github.d4isdavid.educhat.http.request.makeHttpRequest
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class RestClient(
    private val scope: CoroutineScope,
    private val baseUrl: String,
    val headers: MutableMap<String, String> = mutableMapOf()
) {

    private fun <T> request(
        method: String,
        route: String,
        queryParams: Map<String, String>? = null,
        customHeaders: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)? = null,
        hook: HttpURLConnection.() -> T,
    ) {
        val stringUrl = baseUrl + route
        val url = if (queryParams.isNullOrEmpty())
            URL(stringUrl)
        else
            createUrlWithQuery(stringUrl, queryParams)

        scope.launch(Dispatchers.IO + CoroutineName("RestHttpCoroutine")) {
            val result = makeHttpRequest(url) {
                requestMethod = method
                headers.forEach { (k, v) -> setRequestProperty(k, v) }
                customHeaders.forEach { (k, v) -> setRequestProperty(k, v) }
                hook()
            }

            callback?.let { it(result) }
        }
    }

    fun <T> get(
        route: String,
        queryParams: Map<String, String>? = null,
        headers: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)?,
        hook: HttpURLConnection.() -> T,
    ) = request("GET", route, queryParams, headers, callback, hook)

    fun <T> post(
        route: String,
        queryParams: Map<String, String>? = null,
        headers: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)?,
        hook: HttpURLConnection.() -> T,
    ) = request("POST", route, queryParams, headers, callback, hook)

    fun <T> put(
        route: String,
        queryParams: Map<String, String>? = null,
        headers: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)?,
        hook: HttpURLConnection.() -> T,
    ) = request("PUT", route, queryParams, headers, callback, hook)

    fun <T> patch(
        route: String,
        queryParams: Map<String, String>? = null,
        headers: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)?,
        hook: HttpURLConnection.() -> T,
    ) = request("PATCH", route, queryParams, headers, callback, hook)

    fun <T> delete(
        route: String,
        queryParams: Map<String, String>? = null,
        headers: Map<String, String> = mapOf(),
        callback: ((T) -> Unit)?,
        hook: HttpURLConnection.() -> T,
    ) = request("DELETE", route, queryParams, headers, callback, hook)

}
