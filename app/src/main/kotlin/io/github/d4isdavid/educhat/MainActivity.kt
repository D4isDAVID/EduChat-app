package io.github.d4isdavid.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forumSection
import io.github.d4isdavid.educhat.ui.navigation.loginSection
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavHost(navController = navController, startDestination = LOGIN_SECTION_ROUTE) {
                        loginSection(navController = navController)
                        forumSection(navController = navController)
                    }
                }
            }
        }
    }
}
