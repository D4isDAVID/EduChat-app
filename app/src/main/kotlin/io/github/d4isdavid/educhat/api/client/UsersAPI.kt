package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.UserCreateObject
import io.github.d4isdavid.educhat.api.input.UserEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.objects.createUserObject
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.http.rest.RestClient
import io.github.d4isdavid.educhat.http.rest.Routes

class UsersAPI(private val rest: RestClient) {

    val cache = Cache()
    var me: UserObject? = null
        private set

    fun fetch(id: Int, callback: (UserObject) -> Unit) {
        rest.get(Routes.users(id.toString()), callback = callback) {
            val user = createUserObject(handleJsonObject())
            cache.put(user)

            user
        }
    }

    fun get(id: Int, callback: (UserObject) -> Unit) {
        if (cache.has(id))
            return callback(cache.get(id)!!)
        fetch(id, callback)
    }

    fun create(input: UserCreateObject, callback: (UserObject) -> Unit) {
        rest.post(Routes.users(), callback = callback) {
            writeJsonObject(input.toJSON())

            val user = createUserObject(handleJsonObject())
            cache.put(user)

            user
        }
    }

    fun edit(id: Int, input: UserEditObject, callback: (UserObject) -> Unit) {
        rest.patch(Routes.users(id.toString()), callback = callback) {
            writeJsonObject(input.toJSON())

            val user = createUserObject(handleJsonObject())
            cache.put(user)

            user
        }
    }

    fun delete(id: Int, callback: (Unit) -> Unit) {
        rest.delete(Routes.users(id.toString()), callback = callback) {}
    }

    class Cache {

        private val cache = mutableMapOf<Int, UserObject>()

        fun get(id: Int?) = cache[id]

        fun has(id: Int) = cache.contains(id)

        fun put(user: UserObject) {
            cache[user.id] = user
        }

        fun remove(id: Int) {
            cache.remove(id)
        }

    }

}
