package io.github.d4isdavid.educhat.api.client

import io.github.d4isdavid.educhat.api.cache.APICacheWithId
import io.github.d4isdavid.educhat.api.input.CategoryCreateObject
import io.github.d4isdavid.educhat.api.input.CategoryEditObject
import io.github.d4isdavid.educhat.api.input.toJSON
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.createCategoriesList
import io.github.d4isdavid.educhat.api.objects.createCategoryObject
import io.github.d4isdavid.educhat.api.params.CategoriesFetchParams
import io.github.d4isdavid.educhat.api.params.toQuery
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonArray
import io.github.d4isdavid.educhat.http.request.handlers.handleJsonObject
import io.github.d4isdavid.educhat.http.request.writers.writeJsonObject
import io.github.d4isdavid.educhat.http.rest.Routes

class CategoriesAPI(private val client: APIClient) {

    private val rest = client.rest

    val cache = Cache()

    fun fetch(params: CategoriesFetchParams, callback: (List<CategoryObject>) -> Unit) {
        rest.get(Routes.categories(), queryParams = params.toQuery(), callback = callback) {
            val categories = createCategoriesList(handleJsonArray())
            categories.forEach { cache.put(it) }

            categories
        }
    }

    fun fetch(id: Int, callback: (CategoryObject) -> Unit) {
        rest.get(Routes.categories(id.toString()), callback = callback) {
            val category = createCategoryObject(handleJsonObject())
            cache.put(category)

            category
        }
    }

    fun get(id: Int, callback: (CategoryObject) -> Unit) {
        if (cache.has(id))
            return callback(cache.get(id)!!)
        fetch(id, callback)
    }

    fun create(input: CategoryCreateObject, callback: (CategoryObject) -> Unit) {
        rest.post(Routes.categories(), callback = callback) {
            writeJsonObject(input.toJSON())

            val category = createCategoryObject(handleJsonObject())
            cache.put(category)

           category
        }
    }

    fun edit(id: Int, input: CategoryEditObject, callback: (CategoryObject) -> Unit) {
        rest.patch(Routes.categories(id.toString()), callback = callback) {
            writeJsonObject(input.toJSON())

            val category = createCategoryObject(handleJsonObject())
            cache.put(category)

           category
        }
    }

    fun delete(id: Int, callback: (Unit) -> Unit) {
        rest.delete(Routes.categories(id.toString()), callback = callback) {}
    }

    class Cache : APICacheWithId<CategoryObject>()

}
