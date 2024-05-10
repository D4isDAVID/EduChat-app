package io.github.d4isdavid.educhat.api.client

import android.util.Base64
import io.github.d4isdavid.educhat.api.input.AdminUserEditObject
import io.github.d4isdavid.educhat.api.input.UserCreateObject
import io.github.d4isdavid.educhat.api.input.UserEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.http.rest.RestResultListener
import io.github.d4isdavid.educhat.api.utils.Routes

class UsersAPI(client: APIClient) {

    private val rest = client.rest

    val cache = Cache()
    var me: UserObject? = null
        private set

    fun create(input: UserCreateObject) = rest.post(
        Routes.users(),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        rest.headers["Authorization"] = Base64.encodeToString(
            "${input.name}:${input.password}".toByteArray(),
            Base64.NO_WRAP or Base64.NO_PADDING
        )
        me = cache.put(handleJsonObject()!!)
        me!!
    }

    fun get(id: Int) = rest.get(Routes.users(id.toString())) {
        cache.put(handleJsonObject()!!)
    }

    fun edit(id: Int, input: AdminUserEditObject) = rest.patch(
        Routes.users(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun delete(id: Int) = rest.delete(Routes.users(id.toString())) {
        cache.remove(id)
    }

    fun getSelf() = rest.get(Routes.users("@me")) {
        cache.put(handleJsonObject()!!)
    }

    fun editSelf(input: UserEditObject) = rest.patch(
        Routes.users("@me"),
        hook = {
            writeJsonObject(input.toJSON())
        }
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun deleteSelf() = rest.delete(Routes.users("@me")) {
        cache.remove(me!!.id)
        me = null
    }

    fun logIn(name: String, password: String): RestResultListener<UserObject> {
        val auth = Base64.encodeToString(
            "$name:$password".toByteArray(),
            Base64.NO_WRAP or Base64.NO_PADDING
        )

        return rest.get(
            Routes.users("@me"),
            headers = mapOf("Authorization" to auth),
        ) {
            rest.headers["Authorization"] = auth
            me = cache.put(handleJsonObject()!!)
            me!!
        }
    }

    fun logOut() {
        rest.headers.remove("Authorization")
        me = null
    }

    class Cache : APICache<UserObject>(UserObject::class)

}
