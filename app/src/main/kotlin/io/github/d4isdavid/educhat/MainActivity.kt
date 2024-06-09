package io.github.d4isdavid.educhat

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.core.edit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.http.rest.RestClient
import io.github.d4isdavid.educhat.ui.components.dialogs.PermissionRequestDialog
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forumSection
import io.github.d4isdavid.educhat.ui.navigation.loginSection
import io.github.d4isdavid.educhat.ui.navigation.settingsSection
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.CREDENTIALS_EMAIL
import io.github.d4isdavid.educhat.utils.CREDENTIALS_PASSWORD
import io.github.d4isdavid.educhat.utils.SETTINGS_THEME_MODE
import io.github.d4isdavid.educhat.utils.ThemeMode
import io.github.d4isdavid.educhat.utils.askForPermission
import io.github.d4isdavid.educhat.utils.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var showNotificationsDialog by mutableStateOf(false)
        val notificationsLauncher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askForPermission(
                activity = this,
                permission = POST_NOTIFICATIONS,
                onGranted = { showNotificationsDialog = false },
                onDenied = { showNotificationsDialog = true },
            )
        } else {
            null
        }

        val rest = RestClient(BuildConfig.API_BASE_URL, CoroutineScope(Job()))
        val api = APIClient(rest)

        setContent {
            val systemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemDark) }

            val scope = rememberCoroutineScope()
            val navController = rememberNavController()

            var fetching by remember { mutableStateOf(true) }

            EduChatTheme(darkTheme = darkTheme) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (showNotificationsDialog
                        && ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            POST_NOTIFICATIONS
                        )
                    ) {
                        PermissionRequestDialog(
                            permission = POST_NOTIFICATIONS,
                            onConfirm = {
                                showNotificationsDialog = false
                                notificationsLauncher!!.launch(POST_NOTIFICATIONS)
                            },
                            onDismiss = { showNotificationsDialog = false },
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (fetching) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                        return@Surface
                    }

                    NavHost(
                        navController = navController,
                        startDestination = if (api.users.me == null)
                            LOGIN_SECTION_ROUTE
                        else
                            FORUM_SECTION_ROUTE,
                    ) {
                        loginSection(navController = navController, api = api)
                        forumSection(navController = navController, api = api)
                        settingsSection(navController = navController, api = api)
                    }
                }
            }

            LaunchedEffect(key1 = "login") {
                dataStore.data.map {
                    Pair(it[CREDENTIALS_EMAIL], it[CREDENTIALS_PASSWORD])
                }.collect { (email, password) ->
                    if (email == null || password == null) {
                        dataStore.edit { settings -> settings.clear() }
                        fetching = false
                        return@collect
                    }

                    api.users.logIn(email, password)
                        .onSuccess { fetching = false }
                        .onError {
                            scope.launch { dataStore.edit { settings -> settings.clear() } }
                            fetching = false
                        }
                }
            }

            LaunchedEffect(key1 = null) {
                dataStore.data.map { it[SETTINGS_THEME_MODE] }.collect { themeNum ->
                    val theme = ThemeMode.from(themeNum ?: ThemeMode.SystemDefault.num)
                    darkTheme = when (theme) {
                        ThemeMode.SystemDefault -> systemDark
                        ThemeMode.Light -> false
                        ThemeMode.Dark -> true
                    }
                }
            }
        }
    }

}
