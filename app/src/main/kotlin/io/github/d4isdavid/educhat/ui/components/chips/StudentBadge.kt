package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun StudentBadge(modifier: Modifier = Modifier) {
    BadgeChip(
        name = stringResource(id = R.string.student),
        description = stringResource(id = R.string.student_description),
        icon = Icons.Filled.Backpack,
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun StudentBadgePreview() {
    EduChatTheme(dynamicColor = false) {
        StudentBadge()
    }
}
