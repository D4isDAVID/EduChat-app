package io.github.d4isdavid.educhat.ui.navigation.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.settings.AccountPage
import io.github.d4isdavid.educhat.utils.slideInTransition
import io.github.d4isdavid.educhat.utils.slideOutTransition

const val ACCOUNT_PAGE_ROUTE = "account_page"

fun NavGraphBuilder.accountPage(navController: NavController, api: APIClient) {
    composable(
        route = ACCOUNT_PAGE_ROUTE,
        enterTransition = slideInTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideOutTransition,
    ) {
        AccountPage(navController = navController, api = api)
    }
}
