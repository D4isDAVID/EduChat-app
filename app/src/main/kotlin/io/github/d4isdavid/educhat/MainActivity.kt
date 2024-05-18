package io.github.d4isdavid.educhat

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.http.rest.RestClient
import io.github.d4isdavid.educhat.ui.components.dialogs.PermissionRequestDialog
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forumSection
import io.github.d4isdavid.educhat.ui.navigation.loginSection
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.askForPermission

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

        setContent {
            EduChatTheme {
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

                val navController = rememberNavController()

                val rest = RestClient(BuildConfig.API_BASE_URL, rememberCoroutineScope())
                val api = APIClient(rest)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavHost(navController = navController, startDestination = LOGIN_SECTION_ROUTE) {
                        loginSection(navController = navController, api = api)
                        forumSection(navController = navController, api = api)
                    }
                }
            }
        }
    }

}
