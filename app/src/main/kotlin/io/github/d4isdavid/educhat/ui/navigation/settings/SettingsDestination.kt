package io.github.d4isdavid.educhat.ui.navigation.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.settings.SettingsPage

const val SETTINGS_PAGE_ROUTE = "settings_page"

fun NavGraphBuilder.settingsPage(navController: NavController, api: APIClient) {
    composable(route = SETTINGS_PAGE_ROUTE) {
        SettingsPage(navController = navController, api = api)
    }
}
