package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.CategoryPage
import io.github.d4isdavid.educhat.utils.slideDownTransition
import io.github.d4isdavid.educhat.utils.slideUpTransition

private const val CATEGORY_ID_ARGUMENT = "categoryId"
fun categoryPageRoute(id: String = "{$CATEGORY_ID_ARGUMENT}") = "category/$id"

fun NavGraphBuilder.categoryPage(navController: NavController, api: APIClient) {
    composable(
        route = categoryPageRoute(),
        enterTransition = slideUpTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideDownTransition,
    ) {
        val id = it.arguments!!.getString(CATEGORY_ID_ARGUMENT)!!.toInt()

        CategoryPage(
            navController = navController,
            api = api,
            categoryId = id,
        )
    }
}
