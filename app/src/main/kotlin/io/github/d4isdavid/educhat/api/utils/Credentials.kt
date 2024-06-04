package io.github.d4isdavid.educhat.api.utils

import android.util.Base64

fun encodeCredentials(email: String, password: String): String = Base64.encodeToString(
    "$email:$password".toByteArray(),
    Base64.NO_WRAP or Base64.NO_PADDING
)
