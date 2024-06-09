package io.github.d4isdavid.educhat.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbsUpDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun MessageVoteIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = Icons.Outlined.ThumbsUpDown,
        contentDescription = stringResource(id = R.string.message_vote),
        modifier = modifier,
        tint = tint,
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        MessageVoteIcon()
    }
}
