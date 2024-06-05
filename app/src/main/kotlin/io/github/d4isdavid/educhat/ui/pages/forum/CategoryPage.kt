package io.github.d4isdavid.educhat.ui.pages.forum

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.params.CategoriesFetchParams
import io.github.d4isdavid.educhat.api.params.PostsFetchParams
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManageCategoryBottomSheet
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostBottomSheet
import io.github.d4isdavid.educhat.ui.components.buttons.UpFloatingActionButton
import io.github.d4isdavid.educhat.ui.components.icons.AddIcon
import io.github.d4isdavid.educhat.ui.components.icons.CreateIcon
import io.github.d4isdavid.educhat.ui.components.lists.CategoryListItem
import io.github.d4isdavid.educhat.ui.components.lists.PostListItem
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPage(
    navController: NavController,
    api: APIClient,
    modifier: Modifier = Modifier,
    categoryId: Int? = null,
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val onError = errorToSnackbar(scope, snackbarHostState)

    var creating by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf(false) }
    var posting by remember { mutableStateOf(false) }

    var fetching by remember { mutableStateOf(categoryId != null) }
    var fetchingCategories by remember { mutableStateOf(true) }
    var fetchingPosts by remember { mutableStateOf(categoryId != null) }

    var category: CategoryObject? by remember { mutableStateOf(null) }
    val categories = remember { mutableStateListOf<CategoryObject>() }
    val posts = remember { mutableStateListOf<PostObject>() }

    if (inspectionMode) {
        fetchingCategories = false
        categories.add(api.categories.cache.get(1)!!)
        categories.add(api.categories.cache.get(2)!!)
        categories.add(api.categories.cache.get(3)!!)

        if (fetching) {
            fetching = false
            fetchingPosts = false
            category = api.categories.cache.get(categoryId!!)
            posts.add(api.posts.cache.get(1)!!)
            posts.add(api.posts.cache.get(2)!!)
            posts.add(api.posts.cache.get(3)!!)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (categoryId == null)
                            stringResource(id = R.string.forum)
                        else
                            category?.name ?: stringResource(id = R.string.loading)
                    )
                },
                navigationIcon = {
                    if (categoryId == null) {
                        return@TopAppBar
                    }

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    if (categoryId != null && api.users.me != null && api.users.me!!.admin) {
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
            if (fetching) {
                return@Scaffold
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                UpFloatingActionButton(scope = scope, state = listState)

                if (api.users.me != null && api.users.me!!.admin) {
                    Spacer(modifier = Modifier.height(16.dp))

                    SmallFloatingActionButton(
                        onClick = { creating = true },
                        shape = FloatingActionButtonDefaults.smallShape,
                    ) { AddIcon() }
                }

                if (categoryId != null && api.users.me != null && !category!!.locked) {
                    Spacer(modifier = Modifier.height(24.dp))

                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = R.string.write_post)) },
                        icon = { CreateIcon() },
                        onClick = { posting = true },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        if (fetching || fetchingCategories || fetchingPosts) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(categories, key = { "category_${it.id}" }) { category ->
                CategoryListItem(navController = navController, category = category)
            }

            items(posts, key = { "post_${it.messageId}" }) { post ->
                PostListItem(navController = navController, api = api, post = post)
            }
        }
    }

    if (!inspectionMode) {
        api.categories.get(CategoriesFetchParams(categoryId)).onSuccess {
            categories.clear()
            categories.addAll(it)
            fetchingCategories = false
        }.onError { (status, error) -> onError(error.getMessage(context, status)) }

        if (fetching) {
            api.categories.get(categoryId!!).onSuccess {
                category = it
                fetching = false
            }.onError { (status, error) -> onError(error.getMessage(context, status)) }

            api.posts.get(PostsFetchParams(categoryId = categoryId)).onSuccess {
                posts.clear()
                posts.addAll(it)
                fetchingPosts = false
            }.onError { (status, error) -> onError(error.getMessage(context, status)) }
        }
    }

    if (creating) {
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = { creating = false },
            onError = onError,
            parentId = category?.id,
        )
    }

    if (editing) {
        ManageCategoryBottomSheet(
            api = api,
            onDismissRequest = { editing = false },
            onError = onError,
            onDelete = { navController.popBackStack() },
            category = category,
        )
    }

    if (categoryId != null && posting) {
        ManagePostBottomSheet(
            api = api,
            categoryId = categoryId,
            onDismissRequest = { posting = false },
            onError = onError,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(name = "One", parentId = 4)
            mockCategory(id = 2, name = "Two", parentId = 4)
            mockCategory(id = 3, name = "Three", parentId = 4)
            mockCategory(id = 4, name = "Parent")
            mockPost(messageId = 1, title = "One", categoryId = 4)
            mockPost(messageId = 2, title = "Two", categoryId = 4)
            mockPost(messageId = 3, title = "Three", categoryId = 4)
        }
        CategoryPage(
            navController = rememberNavController(),
            api = api,
            categoryId = 4,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun HomePreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(name = "One")
            mockCategory(id = 2, name = "Two")
            mockCategory(id = 3, name = "Three")
            mockPost(messageId = 1, title = "One")
            mockPost(messageId = 2, title = "Two")
            mockPost(messageId = 3, title = "Three")
        }

        CategoryPage(
            navController = rememberNavController(),
            api = api,
        )
    }
}
