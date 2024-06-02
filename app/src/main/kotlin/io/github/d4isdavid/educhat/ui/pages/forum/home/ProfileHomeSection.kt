package io.github.d4isdavid.educhat.ui.pages.forum.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.UserObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockUser
import io.github.d4isdavid.educhat.ui.pages.forum.UserPage
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHomeSection(
    navController: NavController,
    api: APIClient,
    user: UserObject,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.profile))
            })
        }
    ) { paddingValues ->
        UserPage(
            navController = navController,
            api = api,
            user = user,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            showTopBar = false,
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileHomeSectionPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockUser() }
        ProfileHomeSection(
            navController = rememberNavController(),
            api = api,
            user = api.users.cache.get(1)!!,
        )
    }
}
