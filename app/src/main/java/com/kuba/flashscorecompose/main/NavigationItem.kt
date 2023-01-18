package com.kuba.flashscorecompose.main

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 23/12/2022.
 */
sealed class NavigationItem(val route: String, val icon: Int, val title: String) {
    object Home : NavigationItem("home", R.drawable.ic_home, "Home")
    object Explore : NavigationItem("explore", R.drawable.ic_explore, "Explore")
    object Standings : NavigationItem("standings", R.drawable.ic_standings, "Standings")
    object Profile : NavigationItem("profile", R.drawable.ic_profile, "Profile")
}