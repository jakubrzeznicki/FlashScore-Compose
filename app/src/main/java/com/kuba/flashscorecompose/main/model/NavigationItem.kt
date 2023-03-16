package com.kuba.flashscorecompose.main.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by jrzeznicki on 23/12/2022.
 */
sealed class NavigationItem(
    //val direction: DirectionDestination,
    val icon: ImageVector,
    @StringRes val label: Int
) {
//    object Home : NavigationItem(HomeScreenRouteDestination, Icons.Filled.Home, R.string.home)
//    object Explore :
//        NavigationItem(ExploreRouteDestination, Icons.Filled.TravelExplore, R.string.explore)
//
//    object Standings :
//        NavigationItem(StandingsRouteDestination, Icons.Filled.Leaderboard, R.string.standings)
//
//    object Profile : NavigationItem(ProfileRouteDestination, Icons.Filled.Person, R.string.profile)
}
