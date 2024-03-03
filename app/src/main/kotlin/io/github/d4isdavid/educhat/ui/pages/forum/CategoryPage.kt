package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun CategoryPage(navController: NavController, modifier: Modifier = Modifier) {

}

@Composable
@Preview(showBackground = true)
private fun CategoryPagePreview() {
    EduChatTheme(dynamicColor = false) {
        CategoryPage(navController = rememberNavController())
    }
}
