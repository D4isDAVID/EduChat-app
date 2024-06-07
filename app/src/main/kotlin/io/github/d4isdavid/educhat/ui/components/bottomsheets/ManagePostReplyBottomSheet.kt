package io.github.d4isdavid.educhat.ui.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import io.github.d4isdavid.educhat.api.input.AdminPostReplyEditObject
import io.github.d4isdavid.educhat.api.input.PostReplyCreateObject
import io.github.d4isdavid.educhat.api.input.PostReplyEditObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockMessage
import io.github.d4isdavid.educhat.http.request.HttpStatusCode
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledCheckbox
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledIconButton
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ManagePostReplyBottomSheet(
    api: APIClient,
    postId: Int,
    onDismissRequest: () -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
    message: MessageObject? = null,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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

    val (focusRequester) = FocusRequester.createRefs()

    var pinned by remember { mutableStateOf(message?.pinned ?: false) }
    var content by remember { mutableStateOf(message?.content ?: "") }

    var contentError by remember { mutableStateOf("") }
    var fetching by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }

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
                if (message == null) {
                    LabeledIconButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = stringResource(id = R.string.create),
                            )
                        },
                        label = {
                            Text(text = stringResource(id = R.string.create))
                        },
                        onClick = {
                            fetching = true
                            contentError = ""

                            api.posts.createReply(
                                postId,
                                PostReplyCreateObject(content = content),
                            )
                                .onSuccess { hideSheet() }
                                .onError { (status, error) ->
                                    hideSheet()

                                    val errMessage = error.getMessage(context, status)

                                    when (error) {
                                        APIError.BAD_MESSAGE_CONTENT_LENGTH ->
                                            contentError = errMessage

                                        else -> onError(errMessage)
                                    }

                                    fetching = false
                                }
                        },
                        enabled = content.isNotEmpty() && !fetching,
                    )
                    return@Row
                }

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
                        contentError = ""

                        val onEditError: (Pair<HttpStatusCode, APIError>) -> Unit =
                            { (status, error) ->
                                val errMessage = error.getMessage(context, status)

                                when (error) {
                                    APIError.BAD_MESSAGE_CONTENT_LENGTH ->
                                        contentError = errMessage

                                    else -> {
                                        onError(errMessage)
                                        hideSheet()
                                    }
                                }

                                fetching = false
                            }

                        if (api.users.me!!.admin) {
                            api.posts.editReply(
                                postId,
                                message.id,
                                AdminPostReplyEditObject(
                                    content = content,
                                    pinned = pinned,
                                )
                            ).onSuccess { hideSheet() }.onError(onEditError)
                        } else {
                            api.posts.editReply(
                                postId,
                                message.id,
                                PostReplyEditObject(content = content),
                            ).onSuccess { hideSheet() }.onError(onEditError)
                        }
                    },
                    enabled = content.isNotEmpty() && !fetching,
                )

                LabeledIconButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete),
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.delete)) },
                    onClick = { deleting = true },
                    enabled = !fetching,
                )
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                if (message != null && api.users.me?.admin == true) {
                    LabeledCheckbox(
                        checked = pinned,
                        label = { Text(text = stringResource(id = R.string.pinned)) },
                        onCheckedChange = { pinned = it },
                    )
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester = focusRequester),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.content)) },
                    supportingText = if (contentError.isEmpty()) null else ({
                        Text(text = contentError)
                    }),
                    isError = contentError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusRequester.freeFocus() },
                    ),
                )
            }
        }
    }

    if (deleting) {
        AlertDialog(
            onDismissRequest = { deleting = false },
            confirmButton = {
                TextButton(onClick = {
                    fetching = true

                    api.posts.deleteReply(postId, message!!.id)
                        .onSuccess {
                            deleting = false
                            onDelete?.invoke()
                            hideSheet()
                        }
                        .onError { (status, error) ->
                            val errMessage = error.getMessage(context, status)
                            onError(errMessage)
                            fetching = false
                            hideSheet()
                        }
                }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleting = false }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(id = R.string.warning),
                )
            },
            title = { Text(text = stringResource(id = R.string.delete_reply)) },
            text = { Text(text = stringResource(id = R.string.delete_reply_are_you_sure)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun CreatePreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {}
        ManagePostReplyBottomSheet(
            api = api,
            postId = 1,
            onDismissRequest = {},
            onError = {},
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun EditPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockMessage() }
        ManagePostReplyBottomSheet(
            api = api,
            postId = 1,
            onDismissRequest = {},
            onError = {},
            message = api.messages.cache.get(1)!!,
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded,
            )
        )
    }
}
