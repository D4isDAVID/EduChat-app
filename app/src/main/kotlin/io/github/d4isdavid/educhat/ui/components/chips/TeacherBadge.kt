package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun TeacherBadge(modifier: Modifier = Modifier) {
    BadgeChip(
        name = stringResource(id = R.string.teacher),
        description = stringResource(id = R.string.teacher_description),
        icon = Icons.Filled.School,
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun TeacherBadgePreview() {
    EduChatTheme(dynamicColor = false) {
        TeacherBadge()
    }
}
