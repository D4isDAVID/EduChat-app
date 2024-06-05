package io.github.d4isdavid.educhat.ui.navigation.forum.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.SearchPage

const val SEARCH_HOME_SECTION_ROUTE = "search"

fun NavGraphBuilder.searchHomeSection(navController: NavController, api: APIClient) {
    composable(route = SEARCH_HOME_SECTION_ROUTE) {
        SearchPage(
            navController = navController,
            api = api,
        )
    }
}
