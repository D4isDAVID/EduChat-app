package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.utils.createMockReactionCount
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun ReactionChip(
    reaction: MessageObject.ReactionCountObject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    FilterChip(
        selected = reaction.me,
        onClick = onClick,
        label = { Text(text = reaction.count.toString()) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = { Text(text = reaction.emoji) },
    )
}

@Composable
@Preview(showBackground = true)
private fun ReactionChipPreview() {
    EduChatTheme(dynamicColor = false) {
        ReactionChip(
            reaction = MessageObject.ReactionCountObject(createMockReactionCount()),
            onClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ReactionChipReactedPreview() {
    EduChatTheme(dynamicColor = false) {
        ReactionChip(
            reaction = MessageObject.ReactionCountObject(createMockReactionCount(me = true)),
            onClick = {},
        )
    }
}
