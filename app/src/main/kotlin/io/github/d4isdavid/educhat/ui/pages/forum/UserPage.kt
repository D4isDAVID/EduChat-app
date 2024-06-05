package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.input.AdminUserEditObject
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockUser
import io.github.d4isdavid.educhat.ui.components.buttons.BackIconButton
import io.github.d4isdavid.educhat.ui.components.icons.AdminSettingsIcon
import io.github.d4isdavid.educhat.ui.components.lists.UserBadges
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar
import io.github.d4isdavid.educhat.utils.toRelativeString
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(
    navController: NavController,
    api: APIClient,
    modifier: Modifier = Modifier,
    userId: Int? = null,
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val onError = errorToSnackbar(scope, snackbarHostState)

    var user: UserObject? by remember { mutableStateOf(null) }
    if (inspectionMode) {
        user = api.users.cache.get(userId ?: 1)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = user?.name ?: stringResource(id = R.string.loading)) },
                navigationIcon = {
                    if (userId == null) {
                        return@TopAppBar
                    }

                    BackIconButton(navController = navController)
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        if (user == null) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(id = R.string.user),
                    modifier = Modifier
                        .size(48.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = user!!.name, style = MaterialTheme.typography.titleMedium)
            }
            UserBadges(user = user!!)
            Text(
                text = stringResource(
                    id = R.string.joined,
                    Duration.between(user!!.createdAt, Instant.now())
                        .toRelativeString(resources = context.resources),
                ),
                style = MaterialTheme.typography.bodyMedium,
            )

            if (api.users.me?.admin == true) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AdminSettingsIcon(modifier = Modifier.padding(end = 4.dp))
                        Text(
                            text = stringResource(id = R.string.admin_settings),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = {
                                api.users.edit(
                                    user!!.id,
                                    AdminUserEditObject(admin = !user!!.admin)
                                ).onError { (status, error) ->
                                    onError(error.getMessage(context, status))
                                }
                            },
                        ) {
                            Text(
                                text = if (user!!.admin)
                                    stringResource(id = R.string.revoke_admin)
                                else
                                    stringResource(id = R.string.grant_admin),
                            )
                        }

                        Button(
                            onClick = {
                                api.users.edit(
                                    user!!.id,
                                    AdminUserEditObject(helper = !user!!.helper)
                                ).onError { (status, error) ->
                                    onError(error.getMessage(context, status))
                                }
                            },
                        ) {
                            Text(
                                text = if (user!!.helper)
                                    stringResource(id = R.string.revoke_helper)
                                else
                                    stringResource(id = R.string.grant_helper),
                            )
                        }
                    }
                }
            }
        }
    }

    if (!inspectionMode) {
        if (userId == null) {
            api.users.getSelf()
                .onSuccess { user = it }
                .onError { (status, error) -> onError(error.getMessage(context, status)) }
        } else {
            api.users.get(userId)
                .onSuccess { user = it }
                .onError { (status, error) -> onError(error.getMessage(context, status)) }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockUser()
        }

        UserPage(
            navController = rememberNavController(),
            api = api,
            userId = 1,
        )
    }
}
