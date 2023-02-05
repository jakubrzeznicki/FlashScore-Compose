package com.kuba.flashscorecompose.ui.component.chips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 20/01/2023.
 */

sealed class FilterChip(open val textId: Int, open val icon: ImageVector) {
    sealed class Standings(override val textId: Int, override val icon: ImageVector) :
        FilterChip(textId, icon) {

        object All : Standings(R.string.all, Icons.Filled.Subject)
        object Home : Standings(R.string.home, Icons.Filled.Home)
        object Away : Standings(R.string.away, Icons.Filled.FlightTakeoff)
    }

    sealed class Fixtures(override val textId: Int, override val icon: ImageVector) :
        FilterChip(textId, icon) {

        object Played : Fixtures(R.string.played, Icons.Filled.EventAvailable)
        object Upcoming : Fixtures(R.string.upcoming, Icons.Filled.LiveTv)
        object Live : Fixtures(R.string.live, Icons.Filled.Upcoming)
    }

    sealed class Explore(override val textId: Int, override val icon: ImageVector) :
        FilterChip(textId, icon) {

        object Fixtures : Explore(R.string.live_score, Icons.Filled.LiveTv)
        object Teams : Explore(R.string.teams, Icons.Filled.GolfCourse)
        object Players : Explore(R.string.players, Icons.Filled.Groups2)
        object Coaches : Explore(R.string.coaches, Icons.Filled.EmojiPeople)
        object Venues : Explore(R.string.venues, Icons.Filled.Stadium)
        object Leagues : Explore(R.string.leagues, Icons.Filled.Flag)
    }
}