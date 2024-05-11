package io.github.d4isdavid.educhat.ui.components.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.api.input.CategoryCreateObject
import io.github.d4isdavid.educhat.api.input.CategoryEditObject
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledIconButton
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryBottomSheet(
    api: APIClient,
    onDismissRequest: () -> Unit,
    onError: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
    category: CategoryObject? = null,
    parentId: Int? = null,
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

    var name by remember { mutableStateOf(category?.name ?: "") }
    var description: String? by remember { mutableStateOf(category?.description) }

    var nameError by remember { mutableStateOf("") }
    var descriptionError by remember { mutableStateOf("") }
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
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (category == null) {
                    LabeledIconButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = stringResource(id = R.string.save),
                            )
                        },
                        label = {
                            Text(text = stringResource(id = R.string.save))
                        },
                        onClick = {
                            fetching = true
                            nameError = ""
                            descriptionError = ""

                            api.categories.create(CategoryCreateObject(name, description, parentId))
                                .onSuccess { hideSheet() }
                                .onError { (status, error) ->
                                    hideSheet()

                                    val message = error.getMessage(context, status)

                                    when (error) {
                                        APIError.BAD_CATEGORY_NAME_LENGTH,
                                        APIError.INVALID_CATEGORY_NAME -> nameError = message

                                        APIError.BAD_CATEGORY_DESCRIPTION_LENGTH ->
                                            descriptionError = message

                                        else -> onError(message)
                                    }

                                    fetching = false
                                }
                        },
                        enabled = name.isNotEmpty() && !fetching,
                    )
                    return@Row
                }

                LabeledIconButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(id = R.string.save),
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.save)) },
                    onClick = {
                        fetching = true
                        nameError = ""
                        descriptionError = ""

                        api.categories.edit(
                            category.id,
                            CategoryEditObject(
                                name,
                                JSONNullable(description),
                                null,
                                null,
                                null,
                            )
                        ).onSuccess { hideSheet() }.onError { (status, error) ->
                            val message = error.getMessage(context, status)

                            when (error) {
                                APIError.BAD_CATEGORY_NAME_LENGTH,
                                APIError.INVALID_CATEGORY_NAME -> nameError = message

                                APIError.BAD_CATEGORY_DESCRIPTION_LENGTH ->
                                    descriptionError = message

                                else -> onError(message)
                            }

                            hideSheet()
                        }
                    },
                    enabled = name.isNotEmpty() && !fetching,
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.name)) },
                    supportingText = if (nameError.isEmpty()) null else ({
                        Text(text = nameError)
                    }),
                    isError = nameError.isNotEmpty(),
                )

                OutlinedTextField(
                    value = description ?: "",
                    onValueChange = { description = it.ifEmpty { null } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.description)) },
                    supportingText = if (descriptionError.isEmpty()) null else ({
                        Text(text = descriptionError)
                    }),
                    isError = descriptionError.isNotEmpty(),
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

                    api.categories.delete(category!!.id)
                        .onSuccess {
                            hideSheet()
                            onDelete?.invoke()
                        }
                        .onError { (status, error) ->
                            val message = error.getMessage(context, status)
                            onError(message)
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
            title = { Text(text = "Delete Category") },
            text = { Text(text = "Are you sure you want to delete ${category!!.name}?") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun CreateCategoryBottomSheetPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {}
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = {},
            onError = {},
            parentId = 1,
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun EditCategoryBottomSheetPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockCategory() }
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = {},
            onError = {},
            category = api.categories.cache.get(1)!!,
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = LocalDensity.current,
                initialValue = SheetValue.Expanded,
            )
        )
    }
}
