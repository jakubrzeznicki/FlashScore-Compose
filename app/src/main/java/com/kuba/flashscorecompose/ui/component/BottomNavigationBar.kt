package com.kuba.flashscorecompose.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuba.flashscorecompose.navigation.NavGraphs
import com.kuba.flashscorecompose.navigation.model.NavigationItem
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.navigation.popUpTo


/**
 * Created by jrzeznicki on 23/12/2022.
 */
@Composable
fun BottomNavigationBar(tabs: List<NavigationItem>, navController: NavHostController) {
    NavigationBar(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .height(68.dp),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        tabs.forEach { tab ->
            //val isCurrentDestOnBackStack = navController.isRouteOnBackStack(tab.direction)
            NavigationBarItem(
                icon = {
                    Icon(tab.icon, contentDescription = stringResource(id = tab.label))
                },
                label = { Text(text = stringResource(id = tab.label)) },
                alwaysShowLabel = true,
                selected = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedIconColor = MaterialTheme.colorScheme.inverseOnSurface,
                    selectedTextColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                    indicatorColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {
//                    if (isCurrentDestOnBackStack) {
//                        navController.popBackStack(tab.direction, false)
//                        return@NavigationBarItem
//                    }
                    navController.navigateTo(tab.direction) {
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
