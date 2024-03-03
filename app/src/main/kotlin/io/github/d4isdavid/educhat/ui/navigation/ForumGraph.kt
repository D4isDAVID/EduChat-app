package io.github.d4isdavid.educhat.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import io.github.d4isdavid.educhat.ui.navigation.forum.HOME_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.homePage

const val FORUM_SECTION_ROUTE = "forum_section"

fun NavGraphBuilder.forumSection(navController: NavController) {
    navigation(startDestination = HOME_PAGE_ROUTE, route = FORUM_SECTION_ROUTE) {
        homePage(navController = navController)
    }
}
