package io.github.d4isdavid.educhat.ui.components.labeled

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

enum class LabeledCheckboxPosition {
    START,
    END,
}

@Composable
fun LabeledCheckbox(
    checked: Boolean,
    label: @Composable (BoxScope.() -> Unit),
    modifier: Modifier = Modifier,
    spacing: Dp = 6.dp,
    checkboxPosition: LabeledCheckboxPosition = LabeledCheckboxPosition.START,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    @Composable
    fun configuredCheckbox() {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors,
            interactionSource = interactionSource,
        )
    }

    @Composable
    fun configuredSpacer() {
        Spacer(modifier = Modifier.size(spacing))
    }

    @Composable
    fun configuredLabel() {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .clickable {
                    onCheckedChange?.let { it(!checked) }
                },
            contentAlignment = Alignment.Center,
        ) {
            label()
        }
    }

    Row(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (checkboxPosition) {
            LabeledCheckboxPosition.START -> {
                configuredCheckbox()
                configuredSpacer()
                configuredLabel()
            }
            LabeledCheckboxPosition.END -> {
                configuredLabel()
                configuredSpacer()
                configuredCheckbox()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LabeledCheckboxPreview() {
    EduChatTheme(dynamicColor = false) {
        LabeledCheckbox(checked = true, label = { Text(text = "Checkbox") })
    }
}

@Preview(showBackground = true)
@Composable
private fun LabeledCheckboxEndPreview() {
    EduChatTheme(dynamicColor = false) {
        LabeledCheckbox(checked = true, label = { Text(text = "Checkbox") }, checkboxPosition = LabeledCheckboxPosition.END)
    }
}
