package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.navigation.forum.categoryPageRoute
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun CategoryListItem(
    navController: NavController,
    category: CategoryObject,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.Category,
            contentDescription = stringResource(id = R.string.category),
        )
    }
) {
    ListItem(
        headlineContent = {
            Row {
                Text(text = category.name)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                if (category.locked) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = stringResource(id = R.string.locked),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (category.pinned) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = stringResource(id = R.string.pinned),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable { navController.navigate(categoryPageRoute(category.id.toString())) },
        supportingContent = if (category.description != null) ({
            Text(text = category.description!!)
        }) else null,
        leadingContent = { icon() },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(id = R.string.forward),
            )
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun CategoryListItemPreview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) { mockCategory() }
        CategoryListItem(
            navController = rememberNavController(),
            category = api.categories.cache.get(1)!!,
        )
    }
}
