package io.github.d4isdavid.educhat.api.cache

abstract class APICache<T> {

    protected val cache = mutableMapOf<Int, T>()

    fun get(id: Int?) = cache[id]

    fun has(id: Int) = cache.contains(id)

    abstract fun put(obj: T)

    fun remove(id: Int) {
        cache.remove(id)
    }

}
