package io.github.d4isdavid.educhat.http.rest

import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.http.request.HttpStatusCode
import io.github.d4isdavid.educhat.http.request.createUrlWithQuery
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.makeHttpRequest
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class RestClient(
    private val baseUrl: String,
    private val scope: CoroutineScope,
    val headers: MutableMap<String, String> = mutableMapOf()
) {

    private fun <T> request(
        method: String,
        route: String,
        queryParams: Map<String, String?>? = null,
        customHeaders: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ): RestResultListener<T> {
        val stringUrl = baseUrl + route
        val url = if (queryParams.isNullOrEmpty())
            URL(stringUrl)
        else
            createUrlWithQuery(stringUrl, queryParams)

        val listener = RestResultListener<T>()

        scope.launch(Dispatchers.IO + CoroutineName("RestHttpCoroutine")) {
            makeHttpRequest(url) {
                requestMethod = method
                headers.forEach { (k, v) -> setRequestProperty(k, v) }
                customHeaders.forEach { (k, v) -> setRequestProperty(k, v) }
                hook?.let { it() }

                if (responseCode >= 400) {
                    if (listener.error != null) {
                        val obj = handleJsonObject()
                        scope.launch(Dispatchers.Main) {
                            listener.error!!(
                                Pair(
                                    HttpStatusCode.from(responseCode),
                                    if (obj != null && obj.has("code"))
                                        APIError.from(obj.getInt("code"))
                                    else
                                        APIError.GENERIC,
                                )
                            )
                        }
                    }
                    return@makeHttpRequest
                }

                val result = consume()
                listener.success?.let {
                    scope.launch(Dispatchers.Main) {
                        it(result)
                    }
                }
            }
        }

        return listener
    }

    fun <T> get(
        route: String,
        queryParams: Map<String, String?>? = null,
        headers: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ) = request("GET", route, queryParams, headers, hook, consume)

    fun <T> post(
        route: String,
        queryParams: Map<String, String?>? = null,
        headers: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ) = request("POST", route, queryParams, headers, hook, consume)

    fun <T> put(
        route: String,
        queryParams: Map<String, String?>? = null,
        headers: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ) = request("PUT", route, queryParams, headers, hook, consume)

    fun <T> patch(
        route: String,
        queryParams: Map<String, String?>? = null,
        headers: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ) = request("PATCH", route, queryParams, headers, hook, consume)

    fun <T> delete(
        route: String,
        queryParams: Map<String, String?>? = null,
        headers: Map<String, String> = mapOf(),
        hook: (HttpURLConnection.() -> Unit)? = null,
        consume: HttpURLConnection.() -> T,
    ) = request("DELETE", route, queryParams, headers, hook, consume)

}
