package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.params.CategoriesFetchParams
import io.github.d4isdavid.educhat.api.params.CategoryPostsFetchParams
import io.github.d4isdavid.educhat.ui.pages.forum.CategoryPage

private const val CATEGORY_ID_ARGUMENT = "categoryId"
fun categoryPageRoute(id: String = "{$CATEGORY_ID_ARGUMENT}") = "category/$id"

fun NavGraphBuilder.categoryPage(navController: NavController, api: APIClient) {
    composable(route = categoryPageRoute()) {
        val id = it.arguments!!.getString(CATEGORY_ID_ARGUMENT)!!.toInt()

        val categories = remember { mutableStateListOf<CategoryObject>() }
        val posts = remember { mutableStateListOf<Triple<PostObject, MessageObject, UserObject>>() }

        api.categories.get(id)
        api.categories.get(CategoriesFetchParams(parentId = id))
            .onSuccess { list ->
                categories.clear()
                list.forEach { c -> categories.add(c) }
            }
        api.categories.getPosts(id, CategoryPostsFetchParams(null, null, null))
            .onSuccess { list ->
                posts.clear()
                list.forEach { p ->
                    val message = api.messages.cache.get(p.messageId)!!
                    val user = api.users.cache.get(message.authorId)!!
                    posts.add(Triple(p, message, user))
                }
            }

        CategoryPage(
            navController = navController,
            api = api,
            categories = categories,
            posts = posts,
            category = api.categories.cache.get(id),
        )
    }
}
