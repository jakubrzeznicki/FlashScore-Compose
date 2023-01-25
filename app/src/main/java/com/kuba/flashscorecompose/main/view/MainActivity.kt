package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.spec.NavHostEngine

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen() {
        val navController: NavHostController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ) { innerPadding: PaddingValues ->
            FlashScoreNavigation(
                innerPadding = innerPadding,
                navHostController = navController
            )
        }
    }

    @Composable
    fun FlashScoreNavigation(
        innerPadding: PaddingValues,
        navHostController: NavHostController
    ) {
        val navHostEngine: NavHostEngine = rememberNavHostEngine()
        DestinationsNavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navGraph = NavGraphs.root,
            engine = navHostEngine,
            navController = navHostController,
        )
    }
}