package io.github.d4isdavid.educhat.ui.components.chips

import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.d4isdavid.educhat.R

@Composable
fun BadgeChip(
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
