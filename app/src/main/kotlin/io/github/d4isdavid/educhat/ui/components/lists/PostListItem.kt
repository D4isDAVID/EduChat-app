package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun PostListItem(
    post: PostObject,
    message: MessageObject,
    author: UserObject,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Row {
                Text(text = post.title)
                Spacer(modifier = Modifier.width(8.dp))
                if (post.locked) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = stringResource(id = R.string.locked),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (message.pinned) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = stringResource(id = R.string.pinned),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        modifier = modifier,
        overlineContent = { Text(text = author.name) },
        leadingContent = {
            Icon(
                imageVector = if (!post.question)
                    Icons.AutoMirrored.Outlined.Chat
                else if (post.answerId == null)
                    Icons.Outlined.CheckBoxOutlineBlank
                else
                    Icons.Outlined.CheckBox,
                contentDescription = stringResource(id = R.string.post),
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.forward),
            )
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun PostListItemPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockPost() }
        PostListItem(
            api.posts.cache.get(1)!!,
            api.messages.cache.get(1)!!,
            api.users.cache.get(1)!!
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun QuestionPostListItemPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockPost(question = true) }
        PostListItem(
            api.posts.cache.get(1)!!,
            api.messages.cache.get(1)!!,
            api.users.cache.get(1)!!
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AnsweredPostListItemPreview() {
    EduChatTheme(dynamicColor = false) {
        val api =
            createMockClient(rememberCoroutineScope()) { mockPost(question = true, answerId = 1) }
        PostListItem(
            api.posts.cache.get(1)!!,
            api.messages.cache.get(1)!!,
            api.users.cache.get(1)!!
        )
    }
}
