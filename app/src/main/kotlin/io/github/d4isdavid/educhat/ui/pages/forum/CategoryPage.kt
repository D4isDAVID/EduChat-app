package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManageCategoryBottomSheet
import io.github.d4isdavid.educhat.ui.components.lists.CategoryList
import io.github.d4isdavid.educhat.ui.components.lists.PostList
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPage(
    navController: NavController,
    api: APIClient,
    categories: List<CategoryObject>,
    posts: List<Triple<PostObject, MessageObject, UserObject>>,
    modifier: Modifier = Modifier,
    category: CategoryObject? = null,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    var managing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (category == null) {
                return@Scaffold
            }

            TopAppBar(
                title = { Text(text = category.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    if (api.users.me != null && api.users.me!!.admin) {
                        IconButton(onClick = {
                            managing = true
                            scope.launch { sheetState.show() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.edit),
                            )
                        }
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            CategoryList(
                navController = navController,
                categories = categories,
                modifier = Modifier.fillMaxWidth(),
            )
            PostList(posts = posts, modifier = Modifier.fillMaxWidth())
        }
    }

    if (managing) {
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = { managing = false },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
            onDelete = { navController.popBackStack() },
            category = category,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CategoryPagePreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory()
            mockCategory(id = 2, name = "One", parentId = 1)
            mockCategory(id = 3, name = "Two", parentId = 1)
            mockCategory(id = 4, name = "Three", parentId = 1)
            mockPost(messageId = 1, title = "One")
            mockPost(messageId = 2, title = "Two")
            mockPost(messageId = 3, title = "Three")
        }
        val author = api.users.cache.get(1)!!
        CategoryPage(
            navController = rememberNavController(),
            api = api,
            categories = listOf(
                api.categories.cache.get(2)!!,
                api.categories.cache.get(3)!!,
                api.categories.cache.get(4)!!,
            ),
            posts = listOf(
                Triple(api.posts.cache.get(1)!!, api.messages.cache.get(1)!!, author),
                Triple(api.posts.cache.get(2)!!, api.messages.cache.get(2)!!, author),
                Triple(api.posts.cache.get(3)!!, api.messages.cache.get(3)!!, author),
            ),
            category = api.categories.cache.get(1)!!,
        )
    }
}
