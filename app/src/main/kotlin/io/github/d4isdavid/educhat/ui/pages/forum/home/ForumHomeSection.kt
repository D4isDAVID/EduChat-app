package io.github.d4isdavid.educhat.ui.pages.forum.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.pages.forum.CategoryPage
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumHomeSection(
    navController: NavController,
    api: APIClient,
    categories: List<CategoryObject>,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.forum))
            })
        }
    ) { paddingValues ->
        CategoryPage(
            navController = navController,
            api = api,
            categories = categories,
            posts = listOf(),
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
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(id = 1, name = "One")
            mockCategory(id = 2, name = "Two")
            mockCategory(id = 3, name = "Three")
        }
        ForumHomeSection(
            navController = rememberNavController(),
            api = api,
            categories = listOf(
                api.categories.cache.get(1)!!,
                api.categories.cache.get(2)!!,
                api.categories.cache.get(3)!!,
            ),
        )
    }
}
