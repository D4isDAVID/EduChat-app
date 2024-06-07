package io.github.d4isdavid.educhat.api.utils

import android.util.Base64

fun encodeCredentials(credentials: Pair<String, String>): String = Base64.encodeToString(
    "${credentials.first}:${credentials.second}".toByteArray(),
    Base64.NO_WRAP or Base64.NO_PADDING
)
