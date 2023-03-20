package com.kuba.flashscorecompose.main.view

import android.content.res.Resources
import android.util.Log
import androidx.compose.animation.*
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
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.ui.composables.FlashScoreSnackbar
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import com.example.ui.theme.GreenLight
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.navigation.NavGraphs
import com.kuba.flashscorecompose.navigation.NavGraphs.AppNavigation
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.component.NavigationScaffold
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.*
import com.ramcosta.composedestinations.utils.destination
import com.ramcosta.composedestinations.utils.startDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.inject

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlashScoreApp() {
    val snackbarManager: SnackbarManager by inject()
    val appState = rememberAppState(snackbarManager = snackbarManager)
    Log.d("TEST_LOG", "FlashScoreApp - startRoute - ${NavGraphs.root.startRoute}")
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
                Log.d("TEST_LOG", "FlashScoreApp - shouldShowBottomBar - true")
                appState.test()
                BottomNavigationBar(appState.bottomBarTabs, appState.navController)
            } else {
                Log.d("TEST_LOG", "FlashScoreApp - shouldShowBottomBar - false")
                appState.test()
            }
        }
    ) {
        AppNavigation(navController = appState.navController, engine = appState.engine)
//        DestinationsNavHost(
//            engine = appState.engine,
//            navController = appState.navController,
//            navGraph = NavGraphs.root,
//            modifier = Modifier.padding(it),
//            startRoute = NavGraphs.root.startRoute
//        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun rememberAppState(
    engine: NavHostEngine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = { defaultTiviEnterTransition(initialState, targetState) },
            exitTransition = { defaultTiviExitTransition(initialState, targetState) },
            popEnterTransition = { defaultTiviPopEnterTransition() },
            popExitTransition = { defaultTiviPopExitTransition() },
        )
    ),
    navController: NavHostController = rememberAnimatedNavController(),
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
    val selectedItem = remember { mutableStateOf(NavGraphs.home) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            //backQueue.print()
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


@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultTiviPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}
