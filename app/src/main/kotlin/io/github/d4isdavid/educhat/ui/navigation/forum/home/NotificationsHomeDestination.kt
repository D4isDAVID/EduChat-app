package io.github.d4isdavid.educhat.ui.navigation.forum.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.NotificationsPage

const val NOTIFICATIONS_HOME_SECTION_ROUTE = "notifications"

fun NavGraphBuilder.notificationsHomeSection(
    navController: NavController,
    api: APIClient,
    onRefresh: (() -> Unit)? = null,
) {
    composable(route = NOTIFICATIONS_HOME_SECTION_ROUTE) {
        NotificationsPage(
            navController = navController,
            api = api,
            onRefresh = onRefresh,
        )
    }
}
