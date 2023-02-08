package com.kuba.flashscorecompose.splash.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kuba.flashscorecompose.R
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
private const val SPLASH_TIMEOUT = 1500L

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: SplashViewModel = getViewModel()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center,
                painter = painterResource(id = R.drawable.flash_score),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 108.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
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