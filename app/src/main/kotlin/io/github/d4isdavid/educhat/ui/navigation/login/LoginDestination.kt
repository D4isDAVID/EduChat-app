package io.github.d4isdavid.educhat.ui.navigation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.login.LoginPage
import io.github.d4isdavid.educhat.utils.slideInTransition
import io.github.d4isdavid.educhat.utils.slideOutTransition

const val LOGIN_PAGE_ROUTE = "login_page"

fun NavGraphBuilder.loginPage(navController: NavController, api: APIClient) {
    composable(
        route = LOGIN_PAGE_ROUTE,
        enterTransition = slideInTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideOutTransition,
    ) {
        LoginPage(navController = navController, api = api)
    }
}
