package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun AdminBadge(modifier: Modifier = Modifier) {
    BadgeChip(
        name = stringResource(id = R.string.admin),
        description = stringResource(id = R.string.admin_description),
        icon = Icons.Filled.Shield,
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        AdminBadge()
    }
}
