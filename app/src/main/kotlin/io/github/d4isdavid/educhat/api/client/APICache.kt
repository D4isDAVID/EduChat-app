package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.APIObject
import org.json.JSONObject

open class APICache<T : APIObject>(private val clazz: Class<T>) {

    private val _cache = mutableMapOf<Int, T>()

    fun get(key: Int): T? {
        return _cache[key]
    }

    open fun put(key: Int, value: JSONObject) {
        if (!_cache.contains(key)) {
            _cache[key] = clazz.getDeclaredConstructor().newInstance(value)
        }

        _cache[key]!!.update(value)
    }

}