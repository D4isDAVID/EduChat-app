package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun CategoryList(
    categories: List<CategoryObject>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(text = "Categories", style = MaterialTheme.typography.titleMedium)

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
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(id = 1, name = "One")
            mockCategory(id = 2, name = "Two")
            mockCategory(id = 3, name = "Three")
        }
        CategoryList(
            listOf(
                api.categories.cache.get(1)!!,
                api.categories.cache.get(2)!!,
                api.categories.cache.get(3)!!,
            )
        )
    }
}
