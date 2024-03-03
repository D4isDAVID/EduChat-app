package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

private const val FORUM_ROUTE = "forum"
private const val NOTIFICATIONS_ROUTE = "notifications"
private const val SEARCH_ROUTE = "search"
private const val PROFILE_ROUTE = "profile"

@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    val bottomNavController = rememberNavController()
    var currentRoute by remember { mutableStateOf(FORUM_ROUTE) }

    @Composable
    fun RowScope.HomeNavBarItem(
        route: String,
        imageVector: ImageVector,
        imageVectorSelected: ImageVector,
        label: String,
    ) {
        NavigationBarItem(
            selected = currentRoute == route,
            onClick = {
                currentRoute = route
                bottomNavController.popBackStack()
                bottomNavController.navigate(route)
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == route) imageVectorSelected else imageVector,
                    contentDescription = label,
                )
            },
            label = { Text(text = label) }
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                HomeNavBarItem(
                    route = FORUM_ROUTE,
                    imageVector = Icons.Outlined.Forum,
                    imageVectorSelected = Icons.Filled.Forum,
                    label = stringResource(id = R.string.forum),
                )

                HomeNavBarItem(
                    route = NOTIFICATIONS_ROUTE,
                    imageVector = Icons.Outlined.Notifications,
                    imageVectorSelected = Icons.Filled.Notifications,
                    label = stringResource(id = R.string.notifications),
                )

                HomeNavBarItem(
                    route = SEARCH_ROUTE,
                    imageVector = Icons.Outlined.Explore,
                    imageVectorSelected = Icons.Filled.Explore,
                    label = stringResource(id = R.string.search),
                )

                HomeNavBarItem(
                    route = PROFILE_ROUTE,
                    imageVector = Icons.Outlined.Person,
                    imageVectorSelected = Icons.Filled.Person,
                    label = stringResource(id = R.string.profile),
                )
            }
        },
    ) { paddingValues ->
        NavHost(navController = bottomNavController, startDestination = FORUM_ROUTE, modifier = Modifier.padding(paddingValues)) {
            composable(route = FORUM_ROUTE) {}

            composable(route = NOTIFICATIONS_ROUTE) {}

            composable(route = SEARCH_ROUTE) {}

            composable(route = PROFILE_ROUTE) {}
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun HomePagePreview() {
    EduChatTheme(dynamicColor = false) {
        HomePage(navController = rememberNavController())
    }
}
