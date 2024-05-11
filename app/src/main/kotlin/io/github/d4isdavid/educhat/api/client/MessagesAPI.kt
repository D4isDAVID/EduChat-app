package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import org.json.JSONObject
import kotlin.reflect.KFunction

class MessagesAPI(client: APIClient) {

    val cache = Cache(client)

    class Cache(private var client: APIClient) : APICache<MessageObject>(MessageObject::class) {

        fun put(
            value: JSONObject,
            getKey: KFunction<Int>? = null,
            constructor: KFunction<MessageObject>? = null,
            userGetKey: KFunction<Int>? = null,
            userConstructor: KFunction<UserObject>? = null,
        ): MessageObject {
            val user = value.getJSONObject("author")
            client.users.cache.put(user, userGetKey, userConstructor)

            return super.put(value, getKey, constructor)
        }

        override fun put(value: JSONObject, getKey: KFunction<Int>?, constructor: KFunction<MessageObject>?): MessageObject {
            return put(
                value, getKey, constructor,
                null, null,
            )
        }

    }

}
