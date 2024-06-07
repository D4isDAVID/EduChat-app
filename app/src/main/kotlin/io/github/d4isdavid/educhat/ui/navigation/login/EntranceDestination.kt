package io.github.d4isdavid.educhat.ui.navigation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.ui.pages.login.EntrancePage
import io.github.d4isdavid.educhat.utils.slideInTransition
import io.github.d4isdavid.educhat.utils.slideOutTransition

const val ENTRANCE_PAGE_ROUTE = "entrance_page"

fun NavGraphBuilder.entrancePage(navController: NavController) {
    composable(
        route = ENTRANCE_PAGE_ROUTE,
        enterTransition = slideInTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideOutTransition,
    ) {
        EntrancePage(navController = navController)
    }
}
