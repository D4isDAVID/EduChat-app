package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.UserPage

private const val USER_ID_ARGUMENT = "userId"
fun userPageRoute(userId: String = "{$USER_ID_ARGUMENT}") = "user/$userId"

fun NavGraphBuilder.userPage(navController: NavController, api: APIClient) {
    composable(route = userPageRoute()) {
        val id = it.arguments!!.getString(USER_ID_ARGUMENT)!!.toInt()

        UserPage(
            navController = navController,
            api = api,
            userId = id,
        )
    }
}
