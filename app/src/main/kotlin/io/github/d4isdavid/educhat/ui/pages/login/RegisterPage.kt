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
import io.github.d4isdavid.educhat.api.input.UserCreateObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.ui.components.buttons.BackIconButton
import io.github.d4isdavid.educhat.ui.components.icons.RegisterIcon
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledCheckbox
import io.github.d4isdavid.educhat.ui.components.textfields.OutlinedPasswordField
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var student by remember { mutableStateOf(false) }
    var teacher by remember { mutableStateOf(false) }

    var fetching by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.register)) },
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
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !fetching,
                    label = { Text(text = stringResource(id = R.string.username)) },
                    supportingText = if (usernameError.isNotEmpty()) ({
                        Text(text = usernameError)
                    }) else null,
                    isError = usernameError.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth(),
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

                LabeledCheckbox(
                    checked = student,
                    label = { Text(text = stringResource(id = R.string.student)) },
                    onCheckedChange = { student = it },
                    enabled = !fetching,
                )

                LabeledCheckbox(
                    checked = teacher,
                    label = { Text(text = stringResource(id = R.string.teacher)) },
                    onCheckedChange = { teacher = it },
                    enabled = !fetching,
                )
            }

            Button(
                onClick = {
                    fetching = true
                    usernameError = ""
                    emailError = ""
                    passwordError = ""

                    api.users.create(UserCreateObject(username, email, password, student, teacher))
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
                                APIError.USERNAME_UNAVAILABLE,
                                APIError.BAD_USERNAME_LENGTH,
                                APIError.BAD_USERNAME_FORMAT -> usernameError = message

                                APIError.EMAIL_TAKEN,
                                APIError.BAD_EMAIL_FORMAT -> emailError = message

                                APIError.BAD_PASSWORD_LENGTH,
                                APIError.BAD_PASSWORD_FORMAT -> passwordError = message

                                else -> scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message,
                                        withDismissAction = true
                                    )
                                }
                            }

                            fetching = false
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !fetching,
            ) {
                if (fetching) {
                    CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                } else {
                    RegisterIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.register))
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
        RegisterPage(
            navController = rememberNavController(),
            api = api,
        )
    }
}
