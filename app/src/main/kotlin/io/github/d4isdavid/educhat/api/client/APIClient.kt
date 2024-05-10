package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.http.rest.RestClient

class APIClient(val rest: RestClient) {

    val categories = CategoriesAPI(this)
    val messages = MessagesAPI(this)
    val posts = PostsAPI(this)
    val users = UsersAPI(this)

}
