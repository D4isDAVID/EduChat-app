package io.github.d4isdavid.educhat.ui.components.dialogs

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun PermissionRequestDialog(
    permission: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = when (permission) {
        Manifest.permission.POST_NOTIFICATIONS -> Icons.Filled.NotificationsActive
        else -> null
    }

    val messageId = when (permission) {
        Manifest.permission.POST_NOTIFICATIONS -> R.string.notifications_permission_info
        else -> null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.grant_permission))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier,
        icon = {
            if (icon == null) return@AlertDialog
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.permission_icon),
            )
        },
        title = { Text(text = stringResource(id = R.string.missing_permission)) },
        text = {
            if (messageId == null) return@AlertDialog
            Text(text = stringResource(id = messageId))
        }
    )
}

@Composable
@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        PermissionRequestDialog(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onConfirm = {},
            onDismiss = {},
        )
    }
}
