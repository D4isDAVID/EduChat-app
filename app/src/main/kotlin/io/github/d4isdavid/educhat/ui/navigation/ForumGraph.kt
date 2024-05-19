package io.github.d4isdavid.educhat.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.navigation.forum.HOME_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.categoryPage
import io.github.d4isdavid.educhat.ui.navigation.forum.homePage
import io.github.d4isdavid.educhat.ui.navigation.forum.postPage

const val FORUM_SECTION_ROUTE = "forum_section"

fun NavGraphBuilder.forumSection(navController: NavController, api: APIClient) {
    navigation(startDestination = HOME_PAGE_ROUTE, route = FORUM_SECTION_ROUTE) {
        homePage(navController = navController, api = api)
        categoryPage(navController = navController, api = api)
        postPage(navController = navController, api = api)
    }
}
