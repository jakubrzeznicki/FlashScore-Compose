package com.kuba.flashscorecompose.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kuba.flashscorecompose.countries.screen.CountryListScreen
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            FlashScoreComposeTheme {
                NavigationComponent(navController = navController)
            }
        }
    }
}

@Composable
fun NavigationComponent(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "countries"
    ) {
        composable("countries") {
            CountryListScreen(navController)
        }
        composable(
            "country/{countryId}",
            arguments = listOf(navArgument("countryId") { type = NavType.StringType })
        ) {
            val countryId = it.arguments?.getString("countryId") ?: return@composable

        }
    }
}