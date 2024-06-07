package io.github.d4isdavid.educhat.ui.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.api.input.AdminUserEditObject
import io.github.d4isdavid.educhat.api.input.SelfUserEditObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockUser
import io.github.d4isdavid.educhat.http.request.HttpStatusCode
import io.github.d4isdavid.educhat.ui.components.icons.UserIcon
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledIconButton
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsernameBottomSheet(
    api: APIClient,
    onDismissRequest: () -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
    user: UserObject? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var fetching by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(user?.name ?: api.users.me!!.name) }
    var usernameError by remember { mutableStateOf("") }

    fun hideSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets,
        properties = properties,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                LabeledIconButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit),
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.edit)) },
                    onClick = {
                        fetching = true
                        usernameError = ""

                        val onEditError: (Pair<HttpStatusCode, APIError>) -> Unit =
                            { (status, error) ->
                                val message = error.getMessage(context, status)

                                when (error) {
                                    APIError.BAD_USERNAME_LENGTH,
                                    APIError.BAD_USERNAME_FORMAT -> usernameError = message

                                    else -> {
                                        onError(message)
                                        hideSheet()
                                    }
                                }

                                fetching = false
                            }

                        if (user == null) {
                            api.users.editSelf(SelfUserEditObject(name = username))
                                .onSuccess { hideSheet() }
                                .onError(onEditError)
                        } else {
                            api.users.edit(user.id, AdminUserEditObject(name = username))
                                .onSuccess { hideSheet() }
                                .onError(onEditError)
                        }
                    },
                    enabled = !fetching,
                )
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.username)) },
                    leadingIcon = { UserIcon() },
                    supportingText = if (usernameError.isEmpty()) null else ({
                        Text(text = usernameError)
                    }),
                    isError = usernameError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockUser()
        }

        EditUsernameBottomSheet(
            api = api,
            user = api.users.cache.get(1)!!,
            onDismissRequest = {},
            onError = {},
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded,
            )
        )
    }
}
