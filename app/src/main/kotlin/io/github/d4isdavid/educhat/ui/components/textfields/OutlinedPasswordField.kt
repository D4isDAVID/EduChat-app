package io.github.d4isdavid.educhat.ui.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun OutlinedPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    valueVisible: Boolean,
    onValueVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit) = { Text(text = stringResource(id = R.string.password)) },
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = value)) }

    OutlinedPasswordField(
        value = textFieldValue,
        onValueChange = {
            textFieldValue = it
            if (value != it.text)
                onValueChange(it.text)
        },
        valueVisible = valueVisible,
        onValueVisibilityChange = onValueVisibilityChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun OutlinedPasswordField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    valueVisible: Boolean,
    onValueVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit) = { Text(text = stringResource(id = R.string.password)) },
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            trailingIcon?.let { it() } ?: IconButton(onClick = { onValueVisibilityChange(!valueVisible) }) {
                val image = if (valueVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                val descriptionId = if (valueVisible)
                    R.string.hide_password
                else
                    R.string.hide_password

                Icon(imageVector = image, contentDescription = stringResource(id = descriptionId))
            }
        },
        isError = isError,
        visualTransformation = if (valueVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

@Composable
@Preview(showBackground = true)
private fun OutlinedPasswordFieldPreview() {
    EduChatTheme(dynamicColor = false) {
        OutlinedPasswordField(
            value = "",
            onValueChange = {},
            valueVisible = false,
            onValueVisibilityChange = {},
        )
    }
}
