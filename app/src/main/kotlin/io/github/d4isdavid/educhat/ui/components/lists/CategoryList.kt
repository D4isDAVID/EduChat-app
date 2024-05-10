package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.ui.theme.Typography

@Composable
fun CategoryList(
    categories: List<CategoryObject>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(text = "Categories", style = Typography.titleMedium)

        LazyColumn {
            items(categories) {
                CategoryListItem(category = it)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CategoryListPreview() {
    EduChatTheme(dynamicColor = false) {
        CategoryList(
            listOf(
                CategoryObject("One"),
                CategoryObject("Two"),
                CategoryObject("Three"),
            )
        )
    }
}
