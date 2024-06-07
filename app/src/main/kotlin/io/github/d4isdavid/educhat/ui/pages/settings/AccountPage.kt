package io.github.d4isdavid.educhat.ui.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockUser
import io.github.d4isdavid.educhat.ui.components.bottomsheets.EditEmailBottomSheet
import io.github.d4isdavid.educhat.ui.components.bottomsheets.EditPasswordBottomSheet
import io.github.d4isdavid.educhat.ui.components.bottomsheets.EditUsernameBottomSheet
import io.github.d4isdavid.educhat.ui.components.icons.BackIcon
import io.github.d4isdavid.educhat.ui.components.icons.EmailIcon
import io.github.d4isdavid.educhat.ui.components.icons.PasswordIcon
import io.github.d4isdavid.educhat.ui.components.icons.UserIcon
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.errorToSnackbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountPage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val user by remember {
        derivedStateOf {
            if (inspectionMode)
                api.users.cache.get(1)!!
            else
                api.users.me!!
        }
    }
    val userEmail by remember {
        derivedStateOf {
            if (inspectionMode)
                "mock@example.com"
            else
                api.users.credentials!!.first
        }
    }

    var editingUsername by remember { mutableStateOf(false) }
    var editingEmail by remember { mutableStateOf(false) }
    var editingPassword by remember { mutableStateOf(false) }
    val onError = errorToSnackbar(scope, snackbarHostState)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.account)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        BackIcon()
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.username)) },
                supportingContent = { Text(text = user.name) },
                leadingContent = { UserIcon() },
                trailingContent = {
                    TextButton(onClick = { editingUsername = true }) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.email)) },
                supportingContent = { Text(text = userEmail) },
                leadingContent = { EmailIcon() },
                trailingContent = {
                    TextButton(onClick = { editingEmail = true }) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }
            )

            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.password)) },
                leadingContent = { PasswordIcon() },
                trailingContent = {
                    TextButton(onClick = { editingPassword = true }) {
                        Text(text = stringResource(id = R.string.edit))
                    }
                }
            )
        }
    }

    if (editingUsername) {
        EditUsernameBottomSheet(
            api = api,
            onDismissRequest = { editingUsername = false },
            onError = onError,
        )
    }

    if (editingEmail) {
        EditEmailBottomSheet(
            api = api,
            onDismissRequest = { editingEmail = false },
            onError = onError,
        )
    }

    if (editingPassword) {
        EditPasswordBottomSheet(
            api = api,
            onDismissRequest = { editingPassword = false },
            onError = onError,
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

        AccountPage(navController = rememberNavController(), api = api)
    }
}
