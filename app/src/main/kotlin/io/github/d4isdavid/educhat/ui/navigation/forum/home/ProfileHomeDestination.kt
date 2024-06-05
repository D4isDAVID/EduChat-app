package io.github.d4isdavid.educhat.ui.navigation.forum.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.UserPage

const val PROFILE_HOME_SECTION_ROUTE = "profile"

fun NavGraphBuilder.profileHomeSection(navController: NavController, api: APIClient) {
    composable(route = PROFILE_HOME_SECTION_ROUTE) {
        UserPage(
            navController = navController,
            api = api,
            withBackButton = false,
        )
    }
}
