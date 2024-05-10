package io.github.d4isdavid.educhat.ui.pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.BuildConfig
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.http.rest.RestClient
import io.github.d4isdavid.educhat.ui.components.textfields.OutlinedPasswordField
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var fetching by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.username)) },
                    supportingText = if (usernameError.isEmpty()) null else ({
                        Text(text = usernameError)
                    }),
                    isError = usernameError.isNotEmpty(),
                )

                OutlinedPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    valueVisible = passwordVisible,
                    onValueVisibilityChange = { passwordVisible = it },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = if (passwordError.isEmpty()) null else ({
                        Text(text = passwordError)
                    }),
                    isError = usernameError.isNotEmpty(),
                )
            }

            Button(
                onClick = {
                    fetching = true
                    usernameError = ""
                    passwordError = ""

                    api.users.logIn(username, password)
                        .onSuccess {
                            navController.navigate(FORUM_SECTION_ROUTE) {
                                popUpTo(LOGIN_SECTION_ROUTE) {
                                    inclusive = true
                                }
                            }
                        }
                        .onError { (_, error) ->
                            val message = error.getMessage(context)

                            when (error) {
                                APIError.INVALID_USERNAME -> usernameError = message
                                APIError.INVALID_PASSWORD -> passwordError = message

                                else -> scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message,
                                        withDismissAction = true,
                                    )
                                }
                            }

                            fetching = false
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotEmpty() && password.isNotEmpty() && !fetching,
            ) {
                if (fetching) {
                    CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                } else {
                    Text(text = stringResource(id = R.string.login))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    EduChatTheme(dynamicColor = false) {
        LoginPage(
            navController = rememberNavController(),
            api = APIClient(RestClient(BuildConfig.API_BASE_URL, rememberCoroutineScope())),
        )
    }
}
