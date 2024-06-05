package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun HelperBadge(modifier: Modifier = Modifier) {
    BadgeChip(
        name = stringResource(id = R.string.helper),
        description = stringResource(id = R.string.helper_description),
        icon = Icons.Filled.Star,
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        HelperBadge()
    }
}
