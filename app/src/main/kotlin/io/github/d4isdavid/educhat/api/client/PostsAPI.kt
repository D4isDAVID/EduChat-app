package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.PostObject
import org.json.JSONObject

class PostsAPI(client: APIClient) {

    val rest = client.rest

    val cache = Cache(client)

    class Cache(private var client: APIClient) : APICache<PostObject>(PostObject::class.java) {

        override fun put(key: Int, value: JSONObject) {
            super.put(key, value)

            val message = value.getJSONObject("message")
            client.messages.cache.put(message.getInt("id"), message)
        }

    }

}
