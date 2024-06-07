package io.github.d4isdavid.educhat.ui.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
fun SettingsIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = Icons.Filled.Settings,
        contentDescription = stringResource(id = R.string.settings),
        modifier = modifier,
        tint = tint,
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        SettingsIcon()
    }
}
