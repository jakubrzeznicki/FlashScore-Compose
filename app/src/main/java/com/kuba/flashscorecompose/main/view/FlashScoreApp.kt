package com.kuba.flashscorecompose.main.view

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.example.ui.composables.FlashScoreSnackbar
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import com.example.ui.theme.GreenLight
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.navigation.NavGraphs
import com.kuba.flashscorecompose.navigation.NavGraphs.print
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.component.NavigationScaffold
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostEngine
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.inject

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlashScoreApp() {
    val snackbarManager: SnackbarManager by inject()
    val appState = rememberAppState(snackbarManager = snackbarManager)
    NavigationScaffold(
        navController = appState.navController,
        startRoute = NavGraphs.root.startRoute,
        snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier
                    .systemBarsPadding()
                    .wrapContentWidth(align = Alignment.Start)
                    .widthIn(max = 550.dp),
                snackbar = { snackbarData ->
                    FlashScoreSnackbar(
                        snackbarData = snackbarData,
                        containerColor = when (appState.snackbarMessageType.value) {
                            SnackbarMessageType.Error -> MaterialTheme.colorScheme.error
                            SnackbarMessageType.Success -> GreenLight
                        },
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BottomNavigationBar(appState.bottomBarTabs, appState.navController)
            }
        }
    ) {
       // NavGraphs.AppNavigation(appState = appState)
        DestinationsNavHost(
            engine = appState.engine,
            navController = appState.navController,
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(it),
            startRoute = NavGraphs.root.startRoute
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun rememberAppState(
    engine: NavHostEngine = rememberAnimatedNavHostEngine(),
    navController: NavHostController = engine.rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarMessageType: MutableState<SnackbarMessageType> = remember {
        mutableStateOf(SnackbarMessageType.Error)
    },
    snackbarManager: SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(
    engine,
    navController,
    snackbarHostState,
    snackbarManager,
    resources,
    coroutineScope
) {
    FlashScoreAppState(
        engine,
        navController,
        snackbarHostState,
        snackbarMessageType,
        snackbarManager,
        resources,
        coroutineScope
    )
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf(NavGraphs.welcome) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            backQueue.print()
            selectedItem.value = destination.navGraph()
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        NavGraphs.root.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }
    throw RuntimeException("Unknown nav graph for destination $route")
}
