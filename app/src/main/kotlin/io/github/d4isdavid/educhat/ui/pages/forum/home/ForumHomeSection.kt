package io.github.d4isdavid.educhat.ui.pages.forum.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.ui.components.lists.CategoryList
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHomeSection(categories: List<CategoryObject>, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.forum))
            })
        }
    ) { paddingValues ->
        CategoryList(
            categories = categories,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ForumHomeSectionPreview() {
    EduChatTheme(dynamicColor = false) {
        ForumHomeSection(
            listOf(
                CategoryObject("One"),
                CategoryObject("Two"),
                CategoryObject("Three"),
            )
        )
    }
}
