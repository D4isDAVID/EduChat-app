package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.input.CategoryCreateObject
import io.github.d4isdavid.educhat.api.input.CategoryEditObject
import io.github.d4isdavid.educhat.api.input.PostCreateObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.params.CategoriesFetchParams
import io.github.d4isdavid.educhat.api.params.CategoryPostsFetchParams
import io.github.d4isdavid.educhat.api.params.toQuery
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonArray
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.api.utils.Routes

class CategoriesAPI(client: APIClient) {

    private val rest = client.rest

    val cache = Cache()

    fun get(params: CategoriesFetchParams) = rest.get(
        Routes.categories(),
        queryParams = params.toQuery(),
    ) {
        cache.put(handleJsonArray()!!)
    }

    fun create(input: CategoryCreateObject) = rest.post(
        Routes.categories(),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        cache.put(handleJsonObject()!!)
    }

    fun get(id: Int) = rest.get(Routes.categories(id.toString())) {
        cache.put(handleJsonObject()!!)
    }

    fun edit(id: Int, input: CategoryEditObject) = rest.patch(
        Routes.categories(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        handleJsonObject()?.let { cache.put(it) }
    }

    fun delete(id: Int) = rest.delete(Routes.categories(id.toString())) {
        cache.remove(id)
    }

    fun getPosts(id: Int, params: CategoryPostsFetchParams) = rest.get(
        Routes.categoryPosts(id.toString()),
        queryParams = params.toQuery(),
    ) {
        cache.put(handleJsonArray()!!)
    }

    fun createPost(id: Int, input: PostCreateObject) = rest.post(
        Routes.categoryPosts(id.toString()),
        hook = {
            writeJsonObject(input.toJSON())
        },
    ) {
        cache.put(handleJsonObject()!!)
    }

    class Cache : APICache<CategoryObject>(CategoryObject::class)

}
