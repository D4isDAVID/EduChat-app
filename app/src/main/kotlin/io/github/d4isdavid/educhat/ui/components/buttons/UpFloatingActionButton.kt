package io.github.d4isdavid.educhat.ui.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.ui.components.icons.UpIcon
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UpFloatingActionButton(scope: CoroutineScope, state: LazyListState) {
    AnimatedVisibility(visible = state.canScrollBackward) {
        Internal(scope = scope, state = state)
    }
}

@Composable
private fun Internal(scope: CoroutineScope, state: LazyListState) {
    SmallFloatingActionButton(
        onClick = {
            scope.launch {
                state.animateScrollToItem(0)
            }
        },
        shape = FloatingActionButtonDefaults.smallShape,
    ) { UpIcon() }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        Internal(scope = rememberCoroutineScope(), state = rememberLazyListState())
    }
}
