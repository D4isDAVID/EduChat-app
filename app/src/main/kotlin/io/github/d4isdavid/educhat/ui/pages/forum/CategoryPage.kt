package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostBottomSheet
import io.github.d4isdavid.educhat.ui.components.lists.CategoryListItem
import io.github.d4isdavid.educhat.ui.components.lists.PostListItem
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
    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    var creating by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf(false) }
    var posting by remember { mutableStateOf(false) }

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
                        IconButton(onClick = { editing = true }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.edit),
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                AnimatedVisibility(visible = listState.canScrollBackward) {
                    SmallFloatingActionButton(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        shape = FloatingActionButtonDefaults.smallShape,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = stringResource(id = R.string.up),
                        )
                    }
                }

                if (api.users.me != null && api.users.me!!.admin) {
                    Spacer(modifier = Modifier.height(16.dp))

                    SmallFloatingActionButton(
                        onClick = { creating = true },
                        shape = FloatingActionButtonDefaults.smallShape,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(id = R.string.add),
                        )
                    }
                }

                if (category != null && api.users.me != null) {
                    Spacer(modifier = Modifier.height(24.dp))

                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = R.string.write_post)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = stringResource(id = R.string.create),
                            )
                        },
                        onClick = { posting = true }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (categories.isNotEmpty()) {
                item { Text(text = "Categories", style = MaterialTheme.typography.titleMedium) }
                items(categories, key = { "category${it.id}" }) {
                    CategoryListItem(navController = navController, category = it)
                }
            }

            if (posts.isNotEmpty()) {
                item { Text(text = "Posts", style = MaterialTheme.typography.titleMedium) }
                items(
                    posts,
                    key = { (post) -> "post${post.messageId}" }) { (post, message, author) ->
                    PostListItem(
                        navController = navController,
                        post = post,
                        message = message,
                        author = author
                    )
                }
            }
        }
    }

    if (creating) {
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = { creating = false },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
            parentId = category?.id,
        )
    }

    if (editing) {
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = { editing = false },
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

    if (category != null && posting) {
        ManagePostBottomSheet(
            api = api,
            categoryId = category.id,
            onDismissRequest = { posting = false },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
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
