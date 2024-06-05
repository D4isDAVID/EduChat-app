package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.params.PostsFetchParams
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.ui.components.icons.SearchIcon
import io.github.d4isdavid.educhat.ui.components.lists.PostListItem
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf("") }
    val posts = remember { mutableStateListOf<PostObject>() }
    var fetching by remember { mutableStateOf(false) }
    val onError = errorToSnackbar(scope, snackbarHostState)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.search))
            })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        if (fetching) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(paddingValues))
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !fetching,
                label = { Text(text = stringResource(id = R.string.search_posts)) },
                leadingIcon = { SearchIcon() },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        fetching = true
                        api.posts.get(PostsFetchParams(title = title))
                            .onSuccess {
                                posts.clear()
                                posts.addAll(it)
                                fetching = false
                            }
                            .onError { (status, error) ->
                                onError(error.getMessage(context, status))
                            }
                    },
                ),
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(posts.count(), key = { "post${posts[it].messageId}" }) {
                    PostListItem(
                        navController = navController,
                        api = api,
                        post = posts[it],
                    )
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {}

        SearchPage(
            navController = rememberNavController(),
            api = api,
        )
    }
}
