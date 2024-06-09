package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import io.github.d4isdavid.educhat.api.enums.NotificationType
import io.github.d4isdavid.educhat.api.objects.NotificationObject
import io.github.d4isdavid.educhat.api.params.NotificationsFetchParams
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockNotification
import io.github.d4isdavid.educhat.ui.components.icons.MarkAsReadIcon
import io.github.d4isdavid.educhat.ui.components.lists.NotificationListItem
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsPage(
    navController: NavController,
    api: APIClient,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onError = errorToSnackbar(scope, snackbarHostState)

    var fetching by remember { mutableStateOf(true) }
    val notifications = remember { mutableStateListOf<NotificationObject>() }
    if (inspectionMode) {
        fetching = false
        notifications.add(api.notifications.cache.get(1)!!)
        notifications.add(api.notifications.cache.get(2)!!)
        notifications.add(api.notifications.cache.get(3)!!)
        notifications.add(api.notifications.cache.get(4)!!)
        notifications.add(api.notifications.cache.get(5)!!)
        notifications.add(api.notifications.cache.get(6)!!)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.notifications)) },
            )
        },
    ) { paddingValues ->
        if (fetching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                TextButton(
                    onClick = {
                        api.notifications.markAllAsRead()
                            .onSuccess { onRefresh?.invoke() }
                            .onError { (status, error) ->
                                onError(error.getMessage(context, status))
                            }
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    MarkAsReadIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.mark_all_as_read))
                }
            }

            items(notifications, key = { "${it.id}" }) { notification ->
                NotificationListItem(
                    navController = navController,
                    api = api,
                    notification = notification,
                )
            }
        }
    }

    if (!inspectionMode) {
        api.notifications.get(NotificationsFetchParams())
            .onSuccess {
                fetching = false
                notifications.clear()
                notifications.addAll(it)
            }
            .onError { (status, error) -> onError(error.getMessage(context, status)) }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockNotification(id = 1, type = NotificationType.NewPostReply)
            mockNotification(id = 2, type = NotificationType.NewMessageVote)
            mockNotification(id = 3, type = NotificationType.HelperGranted)
            mockNotification(id = 4, type = NotificationType.HelperRevoked)
            mockNotification(id = 5, type = NotificationType.AdminGranted)
            mockNotification(id = 6, type = NotificationType.AdminRevoked)
        }

        NotificationsPage(
            navController = rememberNavController(),
            api = api,
        )
    }
}
