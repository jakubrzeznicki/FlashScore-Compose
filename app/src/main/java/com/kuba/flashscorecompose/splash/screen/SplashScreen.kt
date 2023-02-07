package com.kuba.flashscorecompose.splash.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kuba.flashscorecompose.destinations.HomeScreenRouteDestination
import com.kuba.flashscorecompose.destinations.SplashScreenDestination
import com.kuba.flashscorecompose.destinations.WelcomeRouteDestination
import com.kuba.flashscorecompose.splash.viewmodel.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 06/02/2023.
 */
private const val SPLASH_TIMEOUT = 1000L

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: SplashViewModel = getViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }
    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart(
            openHomeScreen = {
                navigator.navigate(HomeScreenRouteDestination()) {
                    popUpTo(SplashScreenDestination.route) { inclusive = true }
                }
            },
            openWelcomeScreen = {
                navigator.navigate(WelcomeRouteDestination()) {
                    popUpTo(SplashScreenDestination.route) { inclusive = true }
                }
            }
        )
    }
}