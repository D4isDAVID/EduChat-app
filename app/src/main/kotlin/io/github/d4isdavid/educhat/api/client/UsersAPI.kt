package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.UserObject

class UsersAPI(client: APIClient) {

    val rest = client.rest

    val cache = Cache()

    class Cache : APICache<UserObject>(UserObject::class.java)

}
