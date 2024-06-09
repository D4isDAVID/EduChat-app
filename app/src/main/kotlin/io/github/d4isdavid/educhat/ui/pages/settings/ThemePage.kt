package io.github.d4isdavid.educhat.ui.pages.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.MainActivity
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.components.icons.BackIcon
import io.github.d4isdavid.educhat.ui.components.icons.DarkModeIcon
import io.github.d4isdavid.educhat.ui.components.icons.LightModeIcon
import io.github.d4isdavid.educhat.ui.components.icons.SystemDefaultIcon
import io.github.d4isdavid.educhat.ui.components.icons.ThemeIcon
import io.github.d4isdavid.educhat.ui.components.labeled.LabeledRadioButton
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import io.github.d4isdavid.educhat.utils.SETTINGS_THEME_MODE
import io.github.d4isdavid.educhat.utils.ThemeMode
import io.github.d4isdavid.educhat.utils.dataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePage(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var restarting by remember { mutableStateOf(false) }
    var mode: ThemeMode? by remember { mutableStateOf(null) }

    fun setThemeMode(newMode: ThemeMode) {
        if (mode == newMode) {
            return
        }

        scope.launch {
            context.dataStore.edit { settings -> settings[SETTINGS_THEME_MODE] = newMode.num }
            mode = newMode
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.theme)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        BackIcon()
                    }
                },
            )
        },
    ) { paddingValues ->
        if (mode == null) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
            )
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(id = R.string.mode),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )

            LabeledRadioButton(
                selected = mode == ThemeMode.SystemDefault,
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = stringResource(id = R.string.system_default))
                        SystemDefaultIcon()
                    }
                },
                onClick = { setThemeMode(ThemeMode.SystemDefault) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )

            LabeledRadioButton(
                selected = mode == ThemeMode.Light,
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = stringResource(id = R.string.light_mode))
                        LightModeIcon()
                    }
                },
                onClick = { setThemeMode(ThemeMode.Light) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )

            LabeledRadioButton(
                selected = mode == ThemeMode.Dark,
                label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = stringResource(id = R.string.dark_mode))
                        DarkModeIcon()
                    }
                },
                onClick = { setThemeMode(ThemeMode.Dark) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            )
        }
    }

    LaunchedEffect(key1 = null) {
        context.dataStore.data.map { it[SETTINGS_THEME_MODE] }.collect { themeNum ->
            mode = ThemeMode.from(themeNum ?: ThemeMode.SystemDefault.num)
        }
    }

    if (restarting) {
        AlertDialog(
            onDismissRequest = { restarting = false },
            confirmButton = {
                TextButton(onClick = {
                    restarting = false

                    val intent = Intent(context, MainActivity::class.java)
                    intent.setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                    context.startActivity(intent)
                }) {
                    Text(text = stringResource(id = R.string.restart))
                }
            },
            dismissButton = {
                TextButton(onClick = { restarting = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            icon = { ThemeIcon() },
            title = { Text(text = stringResource(id = R.string.apply_theme)) },
            text = { Text(text = stringResource(id = R.string.apply_theme_description)) },
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        ThemePage(navController = rememberNavController())
    }
}
