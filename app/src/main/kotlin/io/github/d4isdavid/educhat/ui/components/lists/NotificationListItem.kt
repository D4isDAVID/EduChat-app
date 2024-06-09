package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.NotificationType
import io.github.d4isdavid.educhat.api.input.NotificationEditObject
import io.github.d4isdavid.educhat.api.objects.NotificationObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockNotification
import io.github.d4isdavid.educhat.ui.components.icons.MessageVoteIcon
import io.github.d4isdavid.educhat.ui.components.icons.OutlinedAdminIcon
import io.github.d4isdavid.educhat.ui.components.icons.OutlinedHelperIcon
import io.github.d4isdavid.educhat.ui.components.icons.PostReplyIcon
import io.github.d4isdavid.educhat.ui.components.icons.ReadIcon
import io.github.d4isdavid.educhat.ui.navigation.forum.postPageRoute
import io.github.d4isdavid.educhat.ui.navigation.forum.userPageRoute
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun NotificationListItem(
    navController: NavController,
    api: APIClient,
    notification: NotificationObject,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val user = notification.userId?.let { api.users.cache.get(it) }
    val post = notification.postId?.let { api.posts.cache.get(it) }

    ListItem(
        headlineContent = {
            Row {
                Text(text = notification.type.getMessage(context))
            }
        },
        modifier = modifier
            .clickable {
                when (notification.type) {
                    NotificationType.NewPostReply,
                    NotificationType.NewMessageVote ->
                        navController.navigate(postPageRoute(post!!.messageId.toString()))

                    NotificationType.HelperGranted,
                    NotificationType.HelperRevoked,
                    NotificationType.AdminGranted,
                    NotificationType.AdminRevoked ->
                        navController.navigate(userPageRoute(api.users.me!!.id.toString()))
                }

                api.notifications.edit(notification.id, NotificationEditObject(read = true))
            },
        supportingContent = {
            Text(text = notification.type.getDescription(context, user!!.name))
        },
        leadingContent = {
            when (notification.type) {
                NotificationType.NewPostReply -> PostReplyIcon()
                NotificationType.NewMessageVote -> MessageVoteIcon()
                NotificationType.HelperGranted -> OutlinedHelperIcon()
                NotificationType.HelperRevoked -> OutlinedHelperIcon()
                NotificationType.AdminGranted -> OutlinedAdminIcon()
                NotificationType.AdminRevoked -> OutlinedAdminIcon()
            }
        },
        trailingContent = {
            if (!notification.read) {
                ReadIcon()
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockNotification()
        }

        NotificationListItem(
            navController = rememberNavController(),
            api = api,
            notification = api.notifications.cache.get(1)!!,
        )
    }
}
