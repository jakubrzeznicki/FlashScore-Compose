package com.kuba.flashscorecompose.navigation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.explore.screen.destinations.ExploreRouteDestination
import com.example.home.screen.destinations.HomeDestination
import com.example.profile.container.screen.destinations.ProfileRouteDestination
import com.example.standings.screen.destinations.StandingsRouteDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.example.ui.R as uiR

/**
 * Created by jrzeznicki on 23/12/2022.
 */
sealed class NavigationItem(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    object Home :
        NavigationItem(HomeDestination, Icons.Filled.Home, uiR.string.home)

    object Explore :
        NavigationItem(ExploreRouteDestination, Icons.Filled.TravelExplore, uiR.string.explore)

    object Standings :
        NavigationItem(StandingsRouteDestination, Icons.Filled.Leaderboard, uiR.string.standings)

    object Profile :
        NavigationItem(ProfileRouteDestination, Icons.Filled.Person, uiR.string.profile)
}
