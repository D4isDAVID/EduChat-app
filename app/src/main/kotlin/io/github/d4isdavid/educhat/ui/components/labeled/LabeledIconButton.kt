package io.github.d4isdavid.educhat.ui.components.labeled

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun LabeledIconButton(
    icon: @Composable (ColumnScope.() -> Unit),
    label: @Composable (ColumnScope.() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {
    ProvideTextStyle(value = MaterialTheme.typography.labelMedium) {
        Surface(
            modifier = modifier
                .width(64.dp)
                .height(64.dp)
                .clickable(enabled = enabled, onClick = onClick),
            color = if (enabled) colors.containerColor else colors.disabledContainerColor,
            contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                icon()
                label()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LabeledIconPreview() {
    EduChatTheme(dynamicColor = false) {
        LabeledIconButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Save",
                )
            },
            label = { Text(text = "Save") },
            onClick = {},
        )
    }
}
