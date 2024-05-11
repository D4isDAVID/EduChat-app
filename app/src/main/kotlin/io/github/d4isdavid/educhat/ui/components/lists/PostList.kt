package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun PostList(
    posts: List<Triple<PostObject, MessageObject, UserObject>>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (posts.isEmpty()) {
            return@Column
        }

        Text(text = "Posts", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(posts, key = { (post) -> post.messageId }) { (post, message, author) ->
                PostListItem(post = post, message = message, author = author)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CategoryListPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockPost(messageId = 1, title = "One")
            mockPost(messageId = 2, title = "Two")
            mockPost(messageId = 3, title = "Three")
        }
        val author = api.users.cache.get(1)!!
        PostList(
            listOf(
                Triple(api.posts.cache.get(1)!!, api.messages.cache.get(1)!!, author),
                Triple(api.posts.cache.get(2)!!, api.messages.cache.get(2)!!, author),
                Triple(api.posts.cache.get(3)!!, api.messages.cache.get(3)!!, author),
            )
        )
    }
}
