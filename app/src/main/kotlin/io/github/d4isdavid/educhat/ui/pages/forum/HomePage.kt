package io.github.d4isdavid.educhat.ui.pages.forum

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.d4isdavid.educhat.R
import io.github.d4isdavid.educhat.api.client.APIClient
import io.github.d4isdavid.educhat.api.objects.CategoryObject
import io.github.d4isdavid.educhat.api.params.CategoriesFetchParams
import io.github.d4isdavid.educhat.api.utils.createMockClient
import io.github.d4isdavid.educhat.api.utils.mockCategory
import io.github.d4isdavid.educhat.ui.navigation.FORUM_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.navigation.LOGIN_SECTION_ROUTE
import io.github.d4isdavid.educhat.ui.pages.forum.home.ForumHomeSection
import io.github.d4isdavid.educhat.ui.theme.EduChatTheme
import kotlinx.coroutines.launch

private const val FORUM_ROUTE = "forum"
private const val NOTIFICATIONS_ROUTE = "notifications"
private const val SEARCH_ROUTE = "search"
private const val PROFILE_ROUTE = "profile"

@Composable
fun HomePage(navController: NavController, api: APIClient, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current

    val scope = rememberCoroutineScope()
    val bottomNavController = rememberNavController()
    var currentRoute by remember { mutableStateOf(FORUM_ROUTE) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                    route = SEARCH_ROUTE,
                    imageVector = Icons.Outlined.Explore,
                    imageVectorSelected = Icons.Filled.Explore,
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
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Login,
                                contentDescription = stringResource(id = R.string.login),
                            )
                        },
                        label = { Text(text = stringResource(id = R.string.login)) }
                    )
                } else {
                    HomeNavBarItem(
                        route = NOTIFICATIONS_ROUTE,
                        imageVector = Icons.Outlined.Notifications,
                        imageVectorSelected = Icons.Filled.Notifications,
                        label = stringResource(id = R.string.notifications),
                    )

                    HomeNavBarItem(
                        route = PROFILE_ROUTE,
                        imageVector = Icons.Outlined.Person,
                        imageVectorSelected = Icons.Filled.Person,
                        label = stringResource(id = R.string.profile),
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        NavHost(navController = bottomNavController, startDestination = FORUM_ROUTE, modifier = Modifier.padding(paddingValues)) {
            composable(route = FORUM_ROUTE) {
                val categories = remember { mutableStateListOf<CategoryObject>() }

                if (!inspectionMode) {
                    api.categories.get(CategoriesFetchParams(parentId = null))
                        .onSuccess {
                            it.forEach { c -> categories.add(c) }
                        }
                        .onError { (status, error) ->
                            scope.launch {
                                snackbarHostState.showSnackbar(error.getMessage(context, status))
                            }
                        }
                } else {
                    categories.add(api.categories.cache.get(1)!!)
                    categories.add(api.categories.cache.get(2)!!)
                    categories.add(api.categories.cache.get(3)!!)
                }

                ForumHomeSection(
                    navController = navController,
                    categories = categories,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }

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
        val api = createMockClient(rememberCoroutineScope()) {
            mockCategory(id = 1, name = "One")
            mockCategory(id = 2, name = "Two")
            mockCategory(id = 3, name = "Three")
        }
        HomePage(navController = rememberNavController(), api = api)
    }
}
