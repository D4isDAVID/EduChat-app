package io.github.d4isdavid.educhat.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

val supportedEmojis = arrayOf("👍", "👎", "😄", "🎉", "😕", "❤️", "🚀", "👀")

@Composable
fun EmojiPickerDialog(
    onEmojiPicked: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.Center,
            ) {
                items(supportedEmojis) {
                    TextButton(onClick = { onEmojiPicked(it) }) {
                        Text(text = it)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmojiPickerDialogPreview() {
    EduChatTheme(dynamicColor = false) {
        EmojiPickerDialog(
            onEmojiPicked = {},
            onDismissRequest = {},
        )
    }
}
