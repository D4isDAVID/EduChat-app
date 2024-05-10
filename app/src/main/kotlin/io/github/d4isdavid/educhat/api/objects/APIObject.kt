package io.github.d4isdavid.educhat.api.objects

import org.json.JSONObject

abstract class APIObject {

    abstract fun update(obj: JSONObject)

}