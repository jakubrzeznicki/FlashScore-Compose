package com.kuba.flashscorecompose.main

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 23/12/2022.
 */
sealed class NavigationItem(val route: String, val icon: Int, val titleId: Int) {
    object Home : NavigationItem("home", R.drawable.ic_home, R.string.home)
    object Explore : NavigationItem("explore", R.drawable.ic_explore, R.string.explore)
    object Standings : NavigationItem("standings", R.drawable.ic_standings, R.string.standings)
    object Profile : NavigationItem("profile", R.drawable.ic_profile, R.string.profile)
}