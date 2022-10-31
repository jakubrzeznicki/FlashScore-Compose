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
import com.kuba.flashscorecompose.leagues.screen.LeaguesListScreen
import com.kuba.flashscorecompose.ui.theme.FlashScoreComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashScoreComposeTheme {
            //    DestinationsNavHost= )
            }
        }
    }
}