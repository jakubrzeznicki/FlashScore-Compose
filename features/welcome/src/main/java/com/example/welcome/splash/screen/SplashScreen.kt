package com.example.welcome.splash.screen

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
import com.example.data.navigation.HomeBackStackType
import com.example.data.navigation.WelcomeBackStackType
import com.example.welcome.R
import com.example.welcome.navigation.WelcomeNavigator
import com.example.welcome.splash.viewmodel.SplashViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 06/02/2023.
 */
private const val SPLASH_TIMEOUT = 1500L

@Destination
@Composable
fun SplashScreen(
    navigator: WelcomeNavigator,
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
            openHomeScreen = { navigator.openHome(HomeBackStackType.Splash) },
            openWelcomeScreen = { navigator.openWelcome(WelcomeBackStackType.Splash) }
        )
    }
}
