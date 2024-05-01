package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.MessageObject
import org.json.JSONObject

class MessagesAPI(client: APIClient) {

    val rest = client.rest

    val cache = Cache(client)

    class Cache(private var client: APIClient) : APICache<MessageObject>(MessageObject::class.java) {

        override fun put(key: Int, value: JSONObject) {
            super.put(key, value)

            val user = value.getJSONObject("author")
            client.users.cache.put(user.getInt("id"), user)
        }

    }

}
