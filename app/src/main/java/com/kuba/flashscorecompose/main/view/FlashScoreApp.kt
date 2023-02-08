package com.kuba.flashscorecompose.main.view

import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.component.FlashScoreSnackbar
import com.kuba.flashscorecompose.ui.component.NavigationScaffold
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import kotlinx.coroutines.CoroutineScope

/**
 * Created by jrzeznicki on 06/02/2023.
 */
@Composable
fun FlashScoreApp() {
    val appState = rememberAppState()
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
                snackbar = { snackbarData -> FlashScoreSnackbar(snackbarData = snackbarData) }
            )
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                BottomNavigationBar(appState.bottomBarTabs, appState.navController)
            }
        }
    ) {
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
    snackbarManager: SnackbarManager = SnackbarManager,
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