package io.github.d4isdavid.educhat.ui.pages.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.navigation.forum.HOME_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.login.LOGIN_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.login.REGISTER_PAGE_ROUTE
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun EntrancePage(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.welcome_to_app),
                style = MaterialTheme.typography.headlineLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { navController.navigate(LOGIN_PAGE_ROUTE) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.login))
            }

            Button(
                onClick = { navController.navigate(REGISTER_PAGE_ROUTE) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.register))
            }

            TextButton(onClick = {
                navController.popBackStack()
                navController.navigate(HOME_PAGE_ROUTE)
            }) {
                Text(text = stringResource(id = R.string.continue_as_guest))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EntrancePagePreview() {
    EduChatTheme(dynamicColor = false) {
        EntrancePage(navController = rememberNavController())
    }
}
