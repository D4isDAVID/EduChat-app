package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockMessage
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostBottomSheet
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostReplyBottomSheet
import io.github.d4isdavid.educhat.ui.components.cards.MessageCard
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPage(
    navController: NavController,
    api: APIClient,
    post: PostObject,
    replies: MutableList<Pair<MessageObject, UserObject>>,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }
    val message = api.messages.cache.get(post.messageId)!!
    val author = api.users.cache.get(message.authorId)!!

    var editing by remember { mutableStateOf(false) }
    var replying by remember { mutableStateOf(false) }
    var editingReply: MessageObject? by remember { mutableStateOf(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            MediumTopAppBar(
                title = { Text(text = post.title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
                actions = {
                    if (api.users.me?.id == author.id) {
                        IconButton(onClick = { editing = true }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.edit),
                            )
                        }
                    }
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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

                Spacer(modifier = Modifier.height(24.dp))

                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(id = R.string.write_reply)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = stringResource(id = R.string.create),
                        )
                    },
                    onClick = { replying = true },
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState,
        ) {
            item {
                MessageCard(
                    navController = navController,
                    api = api,
                    message = message,
                    author = author,
                )

                Text(
                    text = pluralStringResource(
                        id = R.plurals.reply_count,
                        count = replies.size,
                        replies.size
                    ).format(replies.size),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            items(replies, key = { (m) -> "${m.id}" }) { (m, u) ->
                var expanded by remember { mutableStateOf(false) }

                MessageCard(
                    navController = navController,
                    api = api,
                    message = m,
                    author = u,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    trailingIcon = {
                        if (api.users.me?.id != m.authorId) {
                            return@MessageCard
                        }

                        Box(
                            contentAlignment = Alignment.TopEnd,
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Filled.MoreVert,
                                    contentDescription = stringResource(id = R.string.more),
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(id = R.string.edit)) },
                                    onClick = {
                                        expanded = false
                                        editingReply = m
                                    },
                                )
                            }
                        }
                    },
                )
            }
        }
    }

    if (editing) {
        ManagePostBottomSheet(
            api = api,
            categoryId = post.categoryId,
            onDismissRequest = { editing = false },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
            onDelete = {
                navController.popBackStack()
                navController.popBackStack()
            },
            post = post,
        )
    }

    if (replying) {
        ManagePostReplyBottomSheet(
            api = api,
            postId = post.messageId,
            onDismissRequest = { replying = false },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
        )
    }

    if (editingReply != null) {
        ManagePostReplyBottomSheet(
            api = api,
            postId = post.messageId,
            onDismissRequest = { editingReply = null },
            onError = {
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                }
            },
            onDelete = {
                replies.removeIf { (m) -> m.id == editingReply!!.id }
            },
            message = editingReply,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PostPagePreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockPost()
            mockMessage(id = 2, content = "First")
            mockMessage(id = 3, content = "Second")
            mockMessage(id = 4, content = "Third")
        }
        val author = api.users.cache.get(1)!!
        PostPage(
            navController = rememberNavController(),
            api = api,
            post = api.posts.cache.get(1)!!,
            replies = mutableListOf(
                Pair(api.messages.cache.get(2)!!, author),
                Pair(api.messages.cache.get(3)!!, author),
                Pair(api.messages.cache.get(4)!!, author),
            ),
        )
    }
}
