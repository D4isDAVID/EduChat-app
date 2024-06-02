package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun StudentBadge(modifier: Modifier = Modifier) {
    BadgeChip(
        name = stringResource(id = R.string.student),
        description = stringResource(id = R.string.student_description),
        icon = Icons.Filled.Backpack,
        modifier = modifier,
    )
}

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
private fun BadgeChip(
    name: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    var showingDialog by remember { mutableStateOf(false) }

    SuggestionChip(
        onClick = { showingDialog = true },
        label = { Text(text = name, fontSize = 12.sp) },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    )

    if (showingDialog) {
        AlertDialog(
            onDismissRequest = { showingDialog = false },
            confirmButton = {
                TextButton(onClick = { showingDialog = false }) {
                    Text(text = stringResource(id = R.string.close))
                }
            },
            icon = { Icon(imageVector = icon, contentDescription = name) },
            title = { Text(text = name) },
            text = { Text(text = description) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AdminBadgePreview() {
    EduChatTheme(dynamicColor = false) {
        AdminBadge()
    }
}

@Composable
@Preview(showBackground = true)
private fun StudentBadgePreview() {
    EduChatTheme(dynamicColor = false) {
        StudentBadge()
    }
}

@Composable
@Preview(showBackground = true)
private fun TeacherBadgePreview() {
    EduChatTheme(dynamicColor = false) {
        TeacherBadge()
    }
}
