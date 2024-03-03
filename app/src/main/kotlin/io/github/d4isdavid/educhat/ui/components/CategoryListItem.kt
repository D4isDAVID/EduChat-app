package io.github.d4isdavid.educhat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun CategoryListItem(
    name: String,
    description: String?,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.Category,
            contentDescription = stringResource(id = R.string.category),
        )
    }
) {
    ListItem(
        headlineContent = { Text(text = name) },
        modifier = modifier
            .padding(vertical = 8.dp),
        supportingContent = if (description != null) ({
            Text(text = description)
        }) else null,
        leadingContent = { icon() },
        trailingContent = { Text(text = "dasd") },
    )
}

@Composable
@Preview(showBackground = true)
private fun CategoryListItemPreview() {
    EduChatTheme(dynamicColor = false) {
        CategoryListItem(
            name = "Test",
            description = "This is a test category!",
        )
    }
}
