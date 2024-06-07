package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import io.github.d4isdavid.educhat.ui.components.bottomsheets.EditUsernameBottomSheet
import io.github.d4isdavid.educhat.ui.components.buttons.BackIconButton
import io.github.d4isdavid.educhat.ui.components.icons.AdminSettingsIcon
import io.github.d4isdavid.educhat.ui.components.icons.HelperIcon
import io.github.d4isdavid.educhat.ui.components.icons.SettingsIcon
import io.github.d4isdavid.educhat.ui.components.lists.UserBadges
import io.github.d4isdavid.educhat.ui.navigation.SETTINGS_SECTION_ROUTE
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

    var adminMenu by remember { mutableStateOf(false) }
    var editingUsername by remember { mutableStateOf(false) }
    var helperStatus: Boolean? by remember { mutableStateOf(null) }
    var adminStatus: Boolean? by remember { mutableStateOf(null) }

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
                actions = {
                    if (user == null) {
                        return@TopAppBar
                    }

                    if (api.users.me?.admin == true) {
                        IconButton(onClick = { adminMenu = true }) {
                            AdminSettingsIcon()
                        }
                    }

                    if (userId == null) {
                        IconButton(onClick = { navController.navigate(SETTINGS_SECTION_ROUTE) }) {
                            SettingsIcon()
                        }
                    }
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

    if (adminMenu) {
        AlertDialog(
            onDismissRequest = { adminMenu = false },
            confirmButton = {
                TextButton(onClick = { adminMenu = false }) {
                    Text(text = stringResource(id = R.string.close))
                }
            },
            icon = { AdminSettingsIcon() },
            title = { Text(text = stringResource(id = R.string.admin_settings)) },
            text = {
                Column {
                    Button(
                        onClick = {
                            adminMenu = false
                            editingUsername = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = stringResource(id = R.string.edit_username))
                    }

                    if (user!!.helper) {
                        TextButton(
                            onClick = {
                                adminMenu = false
                                helperStatus = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            HelperIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.revoke_helper))
                        }
                    } else {
                        Button(
                            onClick = {
                                adminMenu = false
                                helperStatus = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            HelperIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.grant_helper))
                        }
                    }

                    if (user!!.admin) {
                        TextButton(
                            onClick = {
                                adminMenu = false
                                adminStatus = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            AdminSettingsIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.revoke_admin))
                        }
                    } else {
                        Button(
                            onClick = {
                                adminMenu = false
                                adminStatus = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            AdminSettingsIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.grant_admin))
                        }
                    }
                }
            },
        )
    }

    if (helperStatus != null) {
        val onDismiss = {
            helperStatus = null
            adminMenu = true
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        api.users.edit(user!!.id, AdminUserEditObject(helper = helperStatus!!))
                            .onSuccess { onDismiss() }
                            .onError { (status, error) ->
                                helperStatus = null
                                onError(error.getMessage(context, status))
                            }
                    },
                ) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = { HelperIcon() },
            title = {
                Text(
                    text = stringResource(
                        id = if (helperStatus!!) R.string.grant_helper else R.string.revoke_helper
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = if (helperStatus!!)
                            R.string.grant_helper_are_you_sure
                        else
                            R.string.revoke_helper_are_you_sure
                    )
                )
            }
        )
    }

    if (adminStatus != null) {
        val onDismiss = {
            adminStatus = null
            adminMenu = true
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        api.users.edit(user!!.id, AdminUserEditObject(admin = adminStatus!!))
                            .onSuccess {
                                if (user!!.id == api.users.me!!.id) {
                                    adminStatus = null
                                    return@onSuccess
                                }

                                onDismiss()
                            }
                            .onError { (status, error) ->
                                adminStatus = null
                                onError(error.getMessage(context, status))
                            }
                    },
                ) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            icon = { AdminSettingsIcon() },
            title = {
                Text(
                    text = stringResource(
                        id = if (adminStatus!!) R.string.grant_admin else R.string.revoke_admin
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = if (adminStatus!!)
                            R.string.grant_admin_are_you_sure
                        else
                            R.string.revoke_admin_are_you_sure
                    )
                )
            }
        )
    }

    if (editingUsername) {
        EditUsernameBottomSheet(
            api = api,
            onDismissRequest = {
                adminMenu = true
                editingUsername = false
            },
            onError = onError,
            user = user,
        )
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
