package io.github.d4isdavid.educhat.ui.navigation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.login.RegisterPage
import io.github.d4isdavid.educhat.utils.slideInTransition
import io.github.d4isdavid.educhat.utils.slideOutTransition

const val REGISTER_PAGE_ROUTE = "register_page"

fun NavGraphBuilder.registerPage(navController: NavController, api: APIClient) {
    composable(
        route = REGISTER_PAGE_ROUTE,
        enterTransition = slideInTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideOutTransition,
    ) {
        RegisterPage(navController = navController, api = api)
    }
}
