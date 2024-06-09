package io.github.d4isdavid.educhat.ui.pages.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.ui.components.icons.CloseIcon
import io.github.d4isdavid.educhat.ui.components.icons.ForwardIcon
import io.github.d4isdavid.educhat.ui.components.icons.LogoutIcon
import io.github.d4isdavid.educhat.ui.components.icons.UserIcon
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.settings.ACCOUNT_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.credentials
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        CloseIcon()
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ListItem(
                headlineContent = { Text(text = stringResource(id = R.string.account)) },
                modifier = Modifier.clickable { navController.navigate(ACCOUNT_PAGE_ROUTE) },
                leadingContent = { UserIcon() },
                trailingContent = { ForwardIcon() },
            )

            ListItem(
                headlineContent = {
                    Text(text = stringResource(id = R.string.logout))
                },
                modifier = Modifier.clickable {
                    scope.launch {
                        context.credentials.edit { settings -> settings.clear() }
                        api.users.logOut()
                        navController.navigate(LOGIN_SECTION_ROUTE) {
                            popUpTo(FORUM_SECTION_ROUTE) {
                                inclusive = true
                            }
                        }
                    }
                },
                leadingContent = { LogoutIcon() },
                colors = ListItemDefaults.colors(
                    headlineColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error,
                ),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {}

        SettingsPage(navController = rememberNavController(), api = api)
    }
}
