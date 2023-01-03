package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kuba.flashscorecompose.NavGraphs
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.ui.component.BottomNavigationBar
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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

    @Composable
    fun MainScreen() {
        val navController: NavHostController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) },
//            content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
//                Box(modifier = Modifier.padding(padding)) {
//                    DestinationsNavHost(navGraph = NavGraphs.root)
//                }
//            },
            backgroundColor = MaterialTheme.colors.background
        ) { innerPadding: PaddingValues ->
            ExampleNavigation(
                innerPadding = innerPadding,
                navHostController = navController
            )
        }
    }

    @Composable
    fun ExampleNavigation(
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