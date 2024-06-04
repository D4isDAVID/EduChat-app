package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.AdminUserEditObject
import io.github.d4isdavid.educhat.api.input.UserCreateObject
import io.github.d4isdavid.educhat.api.input.UserEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.Routes
import io.github.d4isdavid.educhat.api.utils.encodeCredentials
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.http.rest.RestResultListener

@Suppress("unused")
class UsersAPI(private val client: APIClient) {

    private val rest
        get() = client.rest

    val cache = Cache()
    var me: UserObject? = null
        private set

    fun create(input: UserCreateObject) = rest.post(
        Routes.users(),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        rest.headers["Authorization"] = "Basic ${encodeCredentials(input.email, input.password)}"
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

    fun logIn(email: String, password: String): RestResultListener<UserObject> {
        val auth = "Basic ${encodeCredentials(email, password)}"

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
