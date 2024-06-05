package io.github.d4isdavid.educhat.ui.pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.enums.APIError
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.ui.components.buttons.BackIconButton
import io.github.d4isdavid.educhat.ui.components.icons.LoginIcon
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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var fetching by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login)) },
                navigationIcon = { BackIconButton(navController = navController) },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.email)) },
                    supportingText = if (emailError.isNotEmpty()) ({
                        Text(text = emailError)
                    }) else null,
                    isError = emailError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )

                OutlinedPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !fetching,
                    supportingText = if (passwordError.isNotEmpty()) ({
                        Text(text = passwordError)
                    }) else null,
                    isError = passwordError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    singleLine = true,
                )
            }

            Button(
                onClick = {
                    fetching = true
                    emailError = ""
                    passwordError = ""

                    api.users.logIn(email, password)
                        .onSuccess {
                            navController.navigate(FORUM_SECTION_ROUTE) {
                                popUpTo(LOGIN_SECTION_ROUTE) {
                                    inclusive = true
                                }
                            }
                        }
                        .onError { (status, error) ->
                            val message = error.getMessage(context, status)

                            when (error) {
                                APIError.INVALID_EMAIL -> emailError = message
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
                enabled = email.isNotEmpty() && password.isNotEmpty() && !fetching,
            ) {
                if (fetching) {
                    CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                } else {
                    LoginIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.login))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {}
        LoginPage(
            navController = rememberNavController(),
            api = api,
        )
    }
}
