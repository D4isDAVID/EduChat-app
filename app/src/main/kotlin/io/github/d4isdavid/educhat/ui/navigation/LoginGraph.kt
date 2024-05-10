package io.github.d4isdavid.educhat.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.navigation.login.ENTRANCE_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.login.entrancePage
import io.github.d4isdavid.educhat.ui.navigation.login.loginPage
import io.github.d4isdavid.educhat.ui.navigation.login.registerPage

const val LOGIN_SECTION_ROUTE = "login_section"

fun NavGraphBuilder.loginSection(navController: NavController, api: APIClient) {
    navigation(startDestination = ENTRANCE_PAGE_ROUTE, route = LOGIN_SECTION_ROUTE) {
        entrancePage(navController = navController)
        registerPage(navController = navController, api = api)
        loginPage(navController = navController, api = api)
    }
}
