package io.github.d4isdavid.educhat.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorPage(
    exceptionMessage: String?,
    exceptionStack: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.error_report)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.error_report_message),
                style = MaterialTheme.typography.titleSmall,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.message),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(text = exceptionMessage ?: stringResource(id = R.string.no_message))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.stack_trace),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exceptionStack,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ErrorPagePreview() {
    EduChatTheme(dynamicColor = false) {
        val e = Error("Mock Error")
        ErrorPage(
            exceptionMessage = e.localizedMessage,
            exceptionStack = e.stackTraceToString(),
            onNavigationClick = {},
        )
    }
}
