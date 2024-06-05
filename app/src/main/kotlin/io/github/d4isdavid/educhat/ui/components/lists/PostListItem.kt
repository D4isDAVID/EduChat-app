package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.components.icons.ForwardIcon
import io.github.d4isdavid.educhat.ui.components.icons.LockedIcon
import io.github.d4isdavid.educhat.ui.components.icons.PinnedIcon
import io.github.d4isdavid.educhat.ui.components.icons.PostIcon
import io.github.d4isdavid.educhat.ui.navigation.forum.postPageRoute
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun PostListItem(
    navController: NavController,
    api: APIClient,
    post: PostObject,
    modifier: Modifier = Modifier,
) {
    val message = api.messages.cache.get(post.messageId)!!
    val author = api.users.cache.get(message.authorId)!!

    ListItem(
        headlineContent = {
            Row {
                Text(text = post.title)
                Spacer(modifier = Modifier.width(8.dp))
                if (post.locked) {
                    LockedIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (message.pinned) {
                    PinnedIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        modifier = modifier
            .clickable { navController.navigate(postPageRoute(post.messageId.toString())) },
        overlineContent = { Text(text = author.name) },
        leadingContent = { PostIcon(post = post) },
        trailingContent = { ForwardIcon() },
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockPost()
        }

        PostListItem(
            navController = rememberNavController(),
            api = api,
            post = api.posts.cache.get(1)!!,
        )
    }
}
