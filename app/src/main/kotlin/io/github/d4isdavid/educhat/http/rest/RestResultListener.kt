package io.github.d4isdavid.educhat.http.rest

import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.http.request.HttpStatusCode

class RestResultListener<S> {

    var success: ((S) -> Unit)? = null
    var error: ((Pair<HttpStatusCode, APIError>) -> Unit)? = null

    infix fun onSuccess(success: ((S) -> Unit)?): RestResultListener<S> {
        this.success = success
        return this
    }

    infix fun onError(error: ((Pair<HttpStatusCode, APIError>) -> Unit)?): RestResultListener<S> {
        this.error = error
        return this
    }

}