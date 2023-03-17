package com.kuba.flashscorecompose.main.view

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessage.Companion.toMessage
import com.example.ui.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.navigation.model.NavigationItem
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.utils.route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@Stable
class FlashScoreAppState(
    val engine: NavHostEngine,
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val snackbarMessageType: MutableState<SnackbarMessageType>,
    val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                snackbarMessageType.value = snackbarMessage.type
                val text = snackbarMessage.toMessage(resources)
                snackbarHostState.showSnackbar(text)
            }
        }
    }

    val bottomBarTabs = listOf(
        NavigationItem.Home,
        NavigationItem.Explore,
        NavigationItem.Standings,
        NavigationItem.Profile
    )
    private val bottomBarDirections = bottomBarTabs.map { it.direction }
    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.route() in bottomBarDirections

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}
