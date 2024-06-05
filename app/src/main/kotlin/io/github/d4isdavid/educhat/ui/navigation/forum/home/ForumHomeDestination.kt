package io.github.d4isdavid.educhat.ui.navigation.forum.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.CategoryPage

const val FORUM_HOME_SECTION_ROUTE = "forum"

fun NavGraphBuilder.forumHomeSection(navController: NavController, api: APIClient) {
    composable(route = FORUM_HOME_SECTION_ROUTE) {
        CategoryPage(
            navController = navController,
            api = api,
        )
    }
}
