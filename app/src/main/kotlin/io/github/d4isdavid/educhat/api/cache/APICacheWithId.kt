package io.github.d4isdavid.educhat.api.cache

open class APICacheWithId<T : APIObjectWithId> : APICache<T>() {

    override fun put(obj: T) {
        cache[obj.id] = obj
    }

}
