package io.github.d4isdavid.educhat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(text = "Hello, world!")
                }
            }
        }
    }
}
