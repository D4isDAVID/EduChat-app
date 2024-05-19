package io.github.d4isdavid.educhat.ui.navigation.forum

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.ui.pages.forum.PostPage

private const val MESSAGE_ID_ARGUMENT = "messageId"
fun postPageRoute(messageId: String = "{$MESSAGE_ID_ARGUMENT}") = "post/$messageId"

fun NavGraphBuilder.postPage(navController: NavController, api: APIClient) {
    composable(route = postPageRoute()) {
        val id = it.arguments!!.getString(MESSAGE_ID_ARGUMENT)!!.toInt()

        val replies = remember { mutableStateListOf<Pair<MessageObject, UserObject>>() }

        api.posts.get(id)
        api.posts.getReplies(id)
            .onSuccess { list ->
                replies.clear()
                list.forEach { m ->
                    val user = api.users.cache.get(m.authorId)!!
                    replies.add(Pair(m, user))
                }
            }

        PostPage(
            navController = navController,
            api = api,
            post = api.posts.cache.get(id)!!,
            replies = replies,
        )
    }
}
