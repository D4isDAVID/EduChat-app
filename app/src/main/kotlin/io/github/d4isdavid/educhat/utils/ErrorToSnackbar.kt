package io.github.d4isdavid.educhat.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun errorToSnackbar(
    scope: CoroutineScope,
    hostState: SnackbarHostState,
): (String) -> Unit = { message ->
    scope.launch {
        hostState.currentSnackbarData?.dismiss()
        hostState.showSnackbar(message, withDismissAction = true)
    }
}
