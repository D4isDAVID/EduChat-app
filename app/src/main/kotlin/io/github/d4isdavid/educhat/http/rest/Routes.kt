package io.github.d4isdavid.educhat.http.rest

object Routes {

    fun categories(id: String? = null) = appendOptional("/categories", id)

    fun categoryPosts(id: String) = "/categories/$id/posts"

    fun posts(id: String) = "/posts/$id"

    fun postReplies(postId: String, id: String? = null) =
        appendOptional("/posts/$postId/replies", id)

    fun users(id: String? = null) = appendOptional("/users", id)

    private fun appendOptional(base: String, str: String? = null) =
        if (str == null) base else "$base/$str"

}
