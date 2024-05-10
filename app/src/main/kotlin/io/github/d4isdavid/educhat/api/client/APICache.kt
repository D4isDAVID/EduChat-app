package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.APIObject
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

open class APICache<out T : APIObject>(private val clazz: KClass<T>) {

    private val _cache = mutableMapOf<Int, T>()

    fun get(key: Int): T? {
        return _cache[key]
    }

    open fun put(value: JSONObject): T {
        val getKey = clazz.companionObject!!.functions.first { it.name == "getKey" }
        val key = getKey.call(clazz.companionObjectInstance, value) as Int

        if (!_cache.contains(key))
            _cache[key] = clazz.primaryConstructor!!.call()

        _cache[key]!!.update(value)
        return _cache[key]!!
    }

    open fun put(values: JSONArray): List<T> {
        return values.getJSONObjects().map { put(it) }
    }

    fun remove(key: Int) {
        _cache.remove(key)
    }

}