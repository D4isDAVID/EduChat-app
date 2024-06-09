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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

enum class LabeledRadioButtonPosition {
    START,
    END,
}

@Composable
fun LabeledRadioButton(
    selected: Boolean,
    label: @Composable (BoxScope.() -> Unit),
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    spacing: Dp = 6.dp,
    radioButtonPosition: LabeledRadioButtonPosition = LabeledRadioButtonPosition.START,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    @Composable
    fun configuredCheckbox() {
        RadioButton(
            selected = selected,
            onClick = onClick,
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
                .clickable { onClick?.let { it() } },
            contentAlignment = Alignment.Center,
        ) {
            label()
        }
    }

    Row(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (radioButtonPosition) {
            LabeledRadioButtonPosition.START -> {
                configuredCheckbox()
                configuredSpacer()
                configuredLabel()
            }

            LabeledRadioButtonPosition.END -> {
                configuredLabel()
                configuredSpacer()
                configuredCheckbox()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        LabeledRadioButton(selected = true, label = { Text(text = "Checkbox") }, onClick = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun EndPreview() {
    EduChatTheme(dynamicColor = false) {
        LabeledRadioButton(
            selected = true,
            label = { Text(text = "Checkbox") },
            onClick = null,
            radioButtonPosition = LabeledRadioButtonPosition.END,
        )
    }
}
