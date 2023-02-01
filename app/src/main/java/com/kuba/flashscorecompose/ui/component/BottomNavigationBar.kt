package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.destinations.*
import com.kuba.flashscorecompose.main.model.NavigationItem
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

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
        val currentRoute = navBackStackEntry?.destination
        items.forEach { item ->
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(item.direction)
            NavigationBarItem(
                icon = {
                    Icon(item.icon, contentDescription = stringResource(id = item.label))
                },
                label = { Text(text = stringResource(id = item.label)) },
                alwaysShowLabel = isCurrentDestOnBackStack,
                selected = isCurrentDestOnBackStack,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                    selectedTextColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navController.popBackStack(item.direction, false)
                        return@NavigationBarItem
                    }
                    navController.navigate(item.direction) {
                        popUpTo(NavGraphs.root) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}