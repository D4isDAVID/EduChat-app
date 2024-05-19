package io.github.d4isdavid.educhat.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.createMockReactionCount
import io.github.d4isdavid.educhat.api.utils.mockMessage
import io.github.d4isdavid.educhat.ui.components.chips.ReactionChip
import io.github.d4isdavid.educhat.ui.components.dialogs.EmojiPickerDialog
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import org.json.JSONArray

@Composable
fun MessageCard(
    api: APIClient,
    message: MessageObject,
    author: UserObject,
    modifier: Modifier = Modifier,
    shape: Shape = CutCornerShape(0.dp),
    colors: CardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.scrim,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContentColor = MaterialTheme.colorScheme.scrim,
    ),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
) {
    var pickingEmoji by remember { mutableStateOf(false) }
    val reactions by remember { derivedStateOf { message.reactions } }

    fun createEmoji(emoji: String) {
        message.putReaction(emoji)
        api.reactions.create(
            messageId = message.id,
            emoji = emoji,
        )
    }

    fun deleteEmoji(emoji: String) {
        message.removeReaction(emoji)
        api.reactions.deleteSelf(
            messageId = message.id,
            emoji = emoji,
        )
    }

    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = author.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                )

                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(id = R.string.more),
                )
            }

            Text(text = message.content, modifier = Modifier.padding(top = 16.dp))

            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(reactions.values.toList(), key = { it.emoji }) {
                    ReactionChip(
                        reaction = it,
                        onClick = {
                            if (it.me) {
                                deleteEmoji(it.emoji)
                                return@ReactionChip
                            }

                            createEmoji(it.emoji)
                        },
                        enabled = api.users.me != null,
                    )
                }

                item {
                    IconButton(
                        onClick = { pickingEmoji = true },
                        enabled = api.users.me != null,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddReaction,
                            contentDescription = stringResource(id = R.string.add_emoji),
                        )
                    }
                }
            }
        }
    }

    if (pickingEmoji) {
        EmojiPickerDialog(
            onEmojiPicked = {
                createEmoji(it)
                pickingEmoji = false
            },
            onDismissRequest = { pickingEmoji = false },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun MessageCardPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockMessage(
                reactions = JSONArray()
                    .put(createMockReactionCount())
                    .put(createMockReactionCount(emoji = "🤓", me = true))
            )
        }
        MessageCard(
            api = api,
            message = api.messages.cache.get(1)!!,
            author = api.users.cache.get(1)!!,
        )
    }
}
