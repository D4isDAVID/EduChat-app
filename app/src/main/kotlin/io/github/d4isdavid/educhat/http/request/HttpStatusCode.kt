package io.github.d4isdavid.educhat.http.request

import android.content.Context
import io.github.d4isdavid.educhat.R

enum class HttpStatusCode(val code: Int) {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_MODIFIED(304),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    GONE(410),
    TOO_MANY_REQUESTS(429),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501);

    fun getMessage(context: Context) = when (this) {
        OK -> context.resources.getString(R.string.http_status_ok)
        CREATED -> context.resources.getString(R.string.http_status_created)
        NO_CONTENT -> context.resources.getString(R.string.http_status_no_content)
        NOT_MODIFIED -> context.resources.getString(R.string.http_status_not_modified)
        BAD_REQUEST -> context.resources.getString(R.string.http_status_bad_request)
        UNAUTHORIZED -> context.resources.getString(R.string.http_status_unauthorized)
        FORBIDDEN -> context.resources.getString(R.string.http_status_forbidden)
        NOT_FOUND -> context.resources.getString(R.string.http_status_not_implemented)
        METHOD_NOT_ALLOWED -> context.resources.getString(R.string.http_status_method_not_allowed)
        CONFLICT -> context.resources.getString(R.string.http_status_conflict)
        GONE -> context.resources.getString(R.string.http_status_gone)
        TOO_MANY_REQUESTS -> context.resources.getString(R.string.http_status_too_many_requests)
        INTERNAL_SERVER_ERROR -> context.resources.getString(R.string.http_status_internal_server_error)
        NOT_IMPLEMENTED -> context.resources.getString(R.string.http_status_not_implemented)
    }

    companion object {
        fun from(code: Int) = entries.first { it.code == code }
    }
}
