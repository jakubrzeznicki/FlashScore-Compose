package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuba.flashscorecompose.main.NavigationItem

/**
 * Created by jrzeznicki on 23/12/2022.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Explore,
        NavigationItem.Standings,
        NavigationItem.Profile
    )
    NavigationBar(
        modifier = Modifier.background(
            color = MaterialTheme.colorScheme.surface
        ),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.titleId)
                    )
                },
                label = { Text(text = stringResource(id = item.titleId)) },
                alwaysShowLabel = item.route == currentRoute,
                selected = currentRoute?.startsWith(item.route) ?: (currentRoute === item.route),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                    selectedTextColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    //   BottomNavigationBar()
}