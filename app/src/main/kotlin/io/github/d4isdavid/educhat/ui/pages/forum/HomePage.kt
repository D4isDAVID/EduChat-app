package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.components.icons.LoginIcon
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.home.FORUM_HOME_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.home.PROFILE_HOME_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.home.SEARCH_HOME_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.forum.home.forumHomeSection
import io.github.d4isdavid.educhat.ui.navigation.forum.home.profileHomeSection
import io.github.d4isdavid.educhat.ui.navigation.forum.home.searchHomeSection
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme

@Composable
fun HomePage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()

    @Composable
    fun RowScope.HomeNavBarItem(
        route: String,
        imageVector: ImageVector,
        imageVectorSelected: ImageVector,
        label: String,
    ) {
        NavigationBarItem(
            selected = navBackStackEntry?.destination?.route == route,
            onClick = {
                bottomNavController.popBackStack()
                bottomNavController.navigate(route)
            },
            icon = {
                Icon(
                    imageVector = if (navBackStackEntry?.destination?.route == route)
                        imageVectorSelected
                    else
                        imageVector,
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
                    route = FORUM_HOME_SECTION_ROUTE,
                    imageVector = Icons.Outlined.Forum,
                    imageVectorSelected = Icons.Filled.Forum,
                    label = stringResource(id = R.string.forum),
                )

                HomeNavBarItem(
                    route = SEARCH_HOME_SECTION_ROUTE,
                    imageVector = Icons.Outlined.Search,
                    imageVectorSelected = Icons.Filled.Search,
                    label = stringResource(id = R.string.search),
                )

                if (api.users.me == null) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(LOGIN_SECTION_ROUTE) {
                                popUpTo(FORUM_SECTION_ROUTE) {
                                    inclusive = true
                                }
                            }
                        },
                        icon = { LoginIcon() },
                        label = { Text(text = stringResource(id = R.string.login)) }
                    )
                } else {
                    HomeNavBarItem(
                        route = PROFILE_HOME_SECTION_ROUTE,
                        imageVector = Icons.Outlined.Person,
                        imageVectorSelected = Icons.Filled.Person,
                        label = stringResource(id = R.string.profile),
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = FORUM_HOME_SECTION_ROUTE,
            modifier = Modifier.padding(paddingValues),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            forumHomeSection(navController = navController, api = api)
            searchHomeSection(navController = navController, api = api)
            profileHomeSection(navController = navController, api = api)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    EduChatTheme(dynamicColor = false) {
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(id = 1, name = "One")
            mockCategory(id = 2, name = "Two")
            mockCategory(id = 3, name = "Three")
        }

        HomePage(navController = rememberNavController(), api = api)
    }
}
