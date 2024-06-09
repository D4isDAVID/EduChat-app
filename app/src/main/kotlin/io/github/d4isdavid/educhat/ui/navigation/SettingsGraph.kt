package io.github.d4isdavid.educhat.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.navigation.settings.SETTINGS_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.settings.accountPage
import io.github.d4isdavid.educhat.ui.navigation.settings.settingsPage
import io.github.d4isdavid.educhat.ui.navigation.settings.themePage

const val SETTINGS_SECTION_ROUTE = "settings_section"

fun NavGraphBuilder.settingsSection(navController: NavController, api: APIClient) {
    navigation(startDestination = SETTINGS_PAGE_ROUTE, route = SETTINGS_SECTION_ROUTE) {
        settingsPage(navController = navController, api = api)
        accountPage(navController = navController, api = api)
        themePage(navController = navController)
    }
}
