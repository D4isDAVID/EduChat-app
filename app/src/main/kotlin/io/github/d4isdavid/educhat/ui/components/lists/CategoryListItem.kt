package io.github.d4isdavid.educhat.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.components.icons.CategoryIcon
import io.github.d4isdavid.educhat.ui.components.icons.ForwardIcon
import io.github.d4isdavid.educhat.ui.components.icons.LockedIcon
import io.github.d4isdavid.educhat.ui.components.icons.PinnedIcon
import io.github.d4isdavid.educhat.ui.navigation.forum.categoryPageRoute
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun CategoryListItem(
    navController: NavController,
    category: CategoryObject,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Row {
                Text(text = category.name)
                Spacer(modifier = Modifier.width(8.dp))
                if (category.locked) {
                    LockedIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (category.pinned) {
                    PinnedIcon(tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        modifier = modifier
            .clickable { navController.navigate(categoryPageRoute(category.id.toString())) },
        supportingContent = if (category.description != null) ({
            Text(text = category.description!!)
        }) else null,
        leadingContent = { CategoryIcon() },
        trailingContent = { ForwardIcon() },
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory()
        }

        CategoryListItem(
            navController = rememberNavController(),
            category = api.categories.cache.get(1)!!,
        )
    }
}
