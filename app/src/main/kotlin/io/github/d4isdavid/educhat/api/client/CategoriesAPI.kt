package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.objects.CategoryObject

class CategoriesAPI(client: APIClient) {

    val rest = client.rest

    val cache = Cache()

    class Cache : APICache<CategoryObject>(CategoryObject::class.java)

}
