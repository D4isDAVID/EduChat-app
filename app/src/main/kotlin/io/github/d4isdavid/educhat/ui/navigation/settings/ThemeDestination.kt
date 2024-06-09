package io.github.d4isdavid.educhat.ui.navigation.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.ui.pages.settings.ThemePage
import io.github.d4isdavid.educhat.utils.slideInTransition
import io.github.d4isdavid.educhat.utils.slideOutTransition

const val THEME_PAGE_ROUTE = "theme_page"

fun NavGraphBuilder.themePage(navController: NavController) {
    composable(
        route = THEME_PAGE_ROUTE,
        enterTransition = slideInTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideOutTransition,
    ) {
        ThemePage(navController = navController)
    }
}
