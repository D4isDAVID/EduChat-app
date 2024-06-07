package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.ui.pages.forum.PostPage
import io.github.d4isdavid.educhat.utils.slideDownTransition
import io.github.d4isdavid.educhat.utils.slideUpTransition

private const val MESSAGE_ID_ARGUMENT = "messageId"
fun postPageRoute(messageId: String = "{$MESSAGE_ID_ARGUMENT}") = "post/$messageId"

fun NavGraphBuilder.postPage(navController: NavController, api: APIClient) {
    composable(
        route = postPageRoute(),
        enterTransition = slideUpTransition,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = slideDownTransition,
    ) {
        val id = it.arguments!!.getString(MESSAGE_ID_ARGUMENT)!!.toInt()

        PostPage(
            navController = navController,
            api = api,
            postId = id,
        )
    }
}
