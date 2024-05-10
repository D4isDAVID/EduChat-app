package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.HomePage

const val HOME_PAGE_ROUTE = "home_page"

fun NavGraphBuilder.homePage(navController: NavController, api: APIClient) {
    composable(route = HOME_PAGE_ROUTE) {
        HomePage(navController = navController, api = api)
    }
}
