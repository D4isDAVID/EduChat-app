package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.MessageObject
import org.json.JSONObject

class MessagesAPI(client: APIClient) {

    val cache = Cache(client)

    class Cache(private var client: APIClient) : APICache<MessageObject>(MessageObject::class) {

        override fun put(value: JSONObject): MessageObject {
            val user = value.getJSONObject("author")
            client.users.cache.put(user)

            return super.put(value)
        }

    }

}
