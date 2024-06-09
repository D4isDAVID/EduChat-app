package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import io.github.d4isdavid.educhat.api.input.PostEditObject
import io.github.d4isdavid.educhat.api.objects.MessageObject
import io.github.d4isdavid.educhat.api.objects.PostObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.JSONNullable
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockMessage
import io.github.d4isdavid.educhat.api.utils.mockPost
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostBottomSheet
import io.github.d4isdavid.educhat.ui.components.bottomsheets.ManagePostReplyBottomSheet
import io.github.d4isdavid.educhat.ui.components.buttons.BackIconButton
import io.github.d4isdavid.educhat.ui.components.buttons.UpFloatingActionButton
import io.github.d4isdavid.educhat.ui.components.cards.MessageCard
import io.github.d4isdavid.educhat.ui.components.icons.CreateIcon
import io.github.d4isdavid.educhat.ui.components.icons.EditIcon
import io.github.d4isdavid.educhat.ui.components.icons.MoreIcon
import io.github.d4isdavid.educhat.ui.components.icons.WarningIcon
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPage(
    navController: NavController,
    api: APIClient,
    postId: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val onError = errorToSnackbar(scope, snackbarHostState)

    var editing by remember { mutableStateOf(false) }
    var replying by remember { mutableStateOf(false) }
    var editingReply: MessageObject? by remember { mutableStateOf(null) }
    var selecting: MessageObject? by remember { mutableStateOf(null) }
    var deselecting by remember { mutableStateOf(false) }

    var post: PostObject? by remember { mutableStateOf(null) }
    var message: MessageObject? by remember { mutableStateOf(null) }
    var author: UserObject? by remember { mutableStateOf(null) }
    var fetchingReplies by remember { mutableStateOf(true) }
    val replies = remember { mutableStateListOf<MessageObject>() }
    if (inspectionMode) {
        post = api.posts.cache.get(1)!!
        message = api.messages.cache.get(1)!!
        author = api.users.cache.get(message!!.authorId)!!
        fetchingReplies = false
        replies.add(api.messages.cache.get(2)!!)
        replies.add(api.messages.cache.get(3)!!)
        replies.add(api.messages.cache.get(4)!!)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = post?.title ?: stringResource(id = R.string.loading)) },
                navigationIcon = { BackIconButton(navController = navController) },
                actions = {
                    if (post == null) {
                        return@TopAppBar
                    }

                    if (api.users.me?.id == author!!.id) {
                        IconButton(onClick = { editing = true }) { EditIcon() }
                    }
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (post == null) {
                return@Scaffold
            }

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                AnimatedVisibility(visible = listState.canScrollBackward) {
                    UpFloatingActionButton(scope = scope, state = listState)
                }

                if (api.users.me != null && !post!!.locked) {
                    Spacer(modifier = Modifier.height(24.dp))

                    ExtendedFloatingActionButton(
                        text = { Text(text = stringResource(id = R.string.write_reply)) },
                        icon = { CreateIcon() },
                        onClick = { replying = true },
                    )
                }
            }
        },
    ) { paddingValues ->
        if (post == null || fetchingReplies) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            )
        }

        if (post == null) {
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                MessageCard(
                    navController = navController,
                    api = api,
                    message = message!!,
                    onError = onError,
                )
            }

            item {
                Text(
                    text = stringResource(id = R.string.replies),
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            items(replies, key = { it.id }) { message ->
                MessageCard(
                    navController = navController,
                    api = api,
                    message = message,
                    onError = onError,
                    answer = post!!.answerId == message.id,
                    trailingIcon = {
                        if (api.users.me?.id != message.authorId) {
                            return@MessageCard
                        }

                        var expanded by remember { mutableStateOf(false) }

                        Box(
                            contentAlignment = Alignment.TopEnd,
                        ) {
                            IconButton(onClick = { expanded = true }) {
                                MoreIcon()
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(id = R.string.edit)) },
                                    onClick = {
                                        expanded = false
                                        editingReply = message
                                    },
                                )

                                if (post!!.answerId == message.id) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(id = R.string.deselect_answer)) },
                                        onClick = {
                                            expanded = false
                                            deselecting = true
                                        },
                                    )
                                } else if (post!!.question) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(id = R.string.select_answer)) },
                                        onClick = {
                                            expanded = false
                                            selecting = message
                                        },
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    if (!inspectionMode) {
        api.posts.get(postId).onSuccess {
            post = it
            message = api.messages.cache.get(post!!.messageId)!!
            author = api.users.cache.get(message!!.authorId)!!
        }.onError { (status, error) -> onError(error.getMessage(context, status)) }

        api.posts.getReplies(postId).onSuccess {
            replies.clear()
            replies.addAll(it)
            fetchingReplies = false
        }.onError { (status, error) -> onError(error.getMessage(context, status)) }
    }

    if (post != null && editing) {
        ManagePostBottomSheet(
            api = api,
            categoryId = post!!.categoryId,
            onDismissRequest = { editing = false },
            onError = onError,
            onDelete = {
                navController.popBackStack()
                navController.popBackStack()
            },
            post = post,
        )
    }

    if (post != null && replying) {
        ManagePostReplyBottomSheet(
            api = api,
            postId = post!!.messageId,
            onDismissRequest = { replying = false },
            onError = onError,
        )
    }

    if (editingReply != null) {
        ManagePostReplyBottomSheet(
            api = api,
            postId = postId,
            onDismissRequest = { editingReply = null },
            onError = { onError(it) },
            message = editingReply,
        )
    }

    if (selecting != null) {
        AlertDialog(
            onDismissRequest = { selecting = null },
            confirmButton = {
                TextButton(onClick = {
                    api.posts.edit(
                        postId,
                        PostEditObject(answerId = JSONNullable(selecting!!.id)),
                    ).onError { (status, error) -> onError(error.getMessage(context, status)) }
                    selecting = null
                }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { selecting = null }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = { WarningIcon() },
            title = { Text(text = stringResource(id = R.string.select_answer)) },
            text = { Text(text = stringResource(id = R.string.select_answer_are_you_sure)) },
        )
    }

    if (deselecting) {
        AlertDialog(
            onDismissRequest = { deselecting = false },
            confirmButton = {
                TextButton(onClick = {
                    api.posts.edit(
                        postId,
                        PostEditObject(answerId = JSONNullable(null)),
                    ).onError { (status, error) -> onError(error.getMessage(context, status)) }
                    deselecting = false
                }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { deselecting = false }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = { WarningIcon() },
            title = { Text(text = stringResource(id = R.string.deselect_answer)) },
            text = { Text(text = stringResource(id = R.string.deselect_answer_are_you_sure)) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockPost()
            mockMessage(id = 2, content = "First")
            mockMessage(id = 3, content = "Second")
            mockMessage(id = 4, content = "Third")
        }
        PostPage(
            navController = rememberNavController(),
            api = api,
            postId = 1,
        )
    }
}
