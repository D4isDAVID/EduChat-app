package io.github.d4isdavid.educhat.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.input.MessageVoteUpsertObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockMessage
import io.github.d4isdavid.educhat.ui.components.lists.UserBadges
import io.github.d4isdavid.educhat.ui.navigation.forum.userPageRoute
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun MessageCard(
    navController: NavController,
    api: APIClient,
    message: MessageObject,
    modifier: Modifier = Modifier,
    answer: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = CutCornerShape(0.dp),
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = if (answer)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
) {
    val author = api.users.cache.get(message.authorId)!!

    ElevatedCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (message.pinned) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = stringResource(id = R.string.pinned),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(16.dp),
                        )

                        Text(
                            text = stringResource(id = R.string.pinned),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }

                if (answer) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(id = R.string.answer),
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(16.dp),
                        )

                        Text(
                            text = stringResource(id = R.string.answer),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = stringResource(id = R.string.user),
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(enabled = api.users.me != null) {
                                navController.navigate(userPageRoute(author.id.toString()))
                            },
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = author.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .clickable(enabled = api.users.me != null) {
                                    navController.navigate(userPageRoute(author.id.toString()))
                                },
                        )

                        UserBadges(user = author)
                    }
                }

                trailingIcon?.invoke()
            }

            Text(text = message.content, modifier = Modifier.padding(vertical = 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        if (message.votes.me == true) {
                            api.votes.deleteSelf(message.id)
                            return@IconButton
                        }

                        api.votes.upsertSelf(message.id, MessageVoteUpsertObject(true))
                    },
                    enabled = api.users.me != null,
                ) {
                    Icon(
                        imageVector = if (message.votes.me == true)
                            Icons.Filled.ThumbUp
                        else
                            Icons.Outlined.ThumbUp,
                        contentDescription = stringResource(id = R.string.upvote),
                    )
                }

                Text(text = message.votes.count.toString())

                IconButton(
                    onClick = {
                        if (message.votes.me == false) {
                            api.votes.deleteSelf(message.id)
                            return@IconButton
                        }

                        api.votes.upsertSelf(message.id, MessageVoteUpsertObject(false))
                    },
                    enabled = api.users.me != null,
                ) {
                    Icon(
                        imageVector = if (message.votes.me == false)
                            Icons.Filled.ThumbDown
                        else
                            Icons.Outlined.ThumbDown,
                        contentDescription = stringResource(id = R.string.downvote),
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockMessage()
        }

        MessageCard(
            navController = rememberNavController(),
            api = api,
            message = api.messages.cache.get(1)!!,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AnswerPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockMessage()
        }

        MessageCard(
            navController = rememberNavController(),
            api = api,
            message = api.messages.cache.get(1)!!,
            answer = true,
        )
    }
}
