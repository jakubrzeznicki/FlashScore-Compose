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
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.component.FlashScoreSnackbar
import com.kuba.flashscorecompose.ui.component.NavigationScaffold
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.ui.theme.GreenLight
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.inject

/**
 * Created by jrzeznicki on 06/02/2023.
 */
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
