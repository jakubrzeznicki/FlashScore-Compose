package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.component.NavigationScaffold
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashScoreComposeTheme {
                MainScreen()
            }
        }
        AppCenter.start(
            application, "bd4b6475-87b1-4e34-b8d9-635973489f20",
            Analytics::class.java, Crashes::class.java
        )
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    @Composable
    fun MainScreen() {
        val engine = rememberAnimatedNavHostEngine()
        val navController = engine.rememberNavController()
        NavigationScaffold(
            navController = navController,
            startRoute = NavGraphs.root.startRoute,
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            DestinationsNavHost(
                engine = engine,
                navController = navController,
                navGraph = NavGraphs.root,
                modifier = Modifier.padding(it),
                startRoute = NavGraphs.root.startRoute
            )
        }
    }
}