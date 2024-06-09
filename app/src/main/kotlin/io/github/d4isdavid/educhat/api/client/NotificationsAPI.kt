package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.NotificationEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.NotificationObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.params.NotificationsFetchParams
import io.github.d4isdavid.educhat.api.params.toQuery
import io.github.d4isdavid.educhat.api.utils.Routes
import io.github.d4isdavid.educhat.api.utils.getJSONObjects
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonArray
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import org.json.JSONObject
import kotlin.reflect.KFunction

class NotificationsAPI(private val client: APIClient) {

    val cache = Cache(client)
    private val rest
        get() = client.rest

    fun get(params: NotificationsFetchParams) = rest.get(
        Routes.notifications(),
        params.toQuery(),
    ) {
        handleJsonArray()!!.getJSONObjects().map {
            cache.put(it)
        }
    }

    fun markAllAsRead() = rest.put(Routes.notifications()) {}

    fun edit(id: Int, input: NotificationEditObject) = rest.patch(
        Routes.notifications(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        }
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun delete(id: Int) = rest.delete(
        Routes.notifications(id.toString()),
    ) {
        cache.remove(id)
    }

    class Cache(
        private val client: APIClient,
    ) : APICache<NotificationObject>(NotificationObject::class) {

        fun put(
            value: JSONObject,
            getKey: KFunction<Int>? = null,
            constructor: KFunction<NotificationObject>? = null,
            userGetKey: KFunction<Int>? = null,
            userConstructor: KFunction<UserObject>? = null,
            postGetKey: KFunction<Int>? = null,
            postConstructor: KFunction<PostObject>? = null,
            messageGetKey: KFunction<Int>? = null,
            messageConstructor: KFunction<MessageObject>? = null,
        ): NotificationObject {
            value.optJSONObject("user")?.let {
                client.users.cache.put(it, userGetKey, userConstructor)
            }
            value.optJSONObject("post")?.let {
                client.posts.cache.put(
                    it,
                    postGetKey,
                    postConstructor,
                    messageGetKey,
                    messageConstructor,
                    userGetKey,
                    userConstructor,
                )
            }
            value.optJSONObject("message")?.let {
                client.messages.cache.put(
                    it,
                    messageGetKey,
                    messageConstructor,
                    userGetKey,
                    userConstructor,
                )
            }

            return super.put(value, getKey, constructor)
        }

        override fun put(
            value: JSONObject,
            getKey: KFunction<Int>?,
            constructor: KFunction<NotificationObject>?,
        ): NotificationObject {
            return put(
                value, getKey, constructor,
                null, null,
                null, null,
                null, null,
            )
        }

    }

}
