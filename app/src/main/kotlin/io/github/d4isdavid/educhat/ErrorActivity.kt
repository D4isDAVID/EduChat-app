package io.github.d4isdavid.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import io.github.d4isdavid.educhat.notifications.ERROR_EXCEPTION_MESSAGE_EXTRA
import io.github.d4isdavid.educhat.notifications.ERROR_EXCEPTION_STACK_EXTRA
import io.github.d4isdavid.educhat.ui.pages.ErrorPage
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

class ErrorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val exceptionMessage = intent.getStringExtra(ERROR_EXCEPTION_MESSAGE_EXTRA)
        val exceptionStack = intent.getStringExtra(ERROR_EXCEPTION_STACK_EXTRA)

        setContent {
            EduChatTheme {
                ErrorPage(
                    exceptionMessage = exceptionMessage!!,
                    exceptionStack = exceptionStack!!,
                    onNavigationClick = { finish() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

}
