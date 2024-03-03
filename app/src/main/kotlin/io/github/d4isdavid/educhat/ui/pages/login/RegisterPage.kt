package io.github.d4isdavid.educhat.ui.pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledCheckbox
import io.github.d4isdavid.educhat.ui.components.textfields.OutlinedPasswordField
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
             TopAppBar(
                 title = { Text(text = stringResource(id = R.string.register)) },
                 navigationIcon = {
                      IconButton(onClick = { navController.popBackStack() }) {
                          Icon(
                              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                              contentDescription = stringResource(id = R.string.back)
                          )
                      }
                 },
             )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(paddingValues),
        ) {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            var student by remember { mutableStateOf(false) }
            var teacher by remember { mutableStateOf(false) }
            var admin by remember { mutableStateOf(false) }

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
                )

                OutlinedPasswordField(
                    value = password,
                    onValueChange = { password = it },
                    valueVisible = passwordVisible,
                    onValueVisibilityChange = { passwordVisible = it },
                    modifier = Modifier.fillMaxWidth(),
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LabeledCheckbox(
                        checked = student,
                        label = { Text(text = stringResource(id = R.string.student)) },
                        onCheckedChange = { student = it },
                    )

                    LabeledCheckbox(
                        checked = teacher,
                        label = { Text(text = stringResource(id = R.string.teacher)) },
                        onCheckedChange = { teacher = it },
                    )

                    LabeledCheckbox(
                        checked = admin,
                        label = { Text(text = stringResource(id = R.string.admin)) },
                        onCheckedChange = { admin = it },
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
            ) {
               Text(text = stringResource(id = R.string.register))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPagePreview() {
    EduChatTheme(dynamicColor = false) {
        RegisterPage(rememberNavController())
    }
}
