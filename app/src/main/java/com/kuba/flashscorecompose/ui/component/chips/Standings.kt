package com.kuba.flashscorecompose.ui.component.chips

import com.kuba.flashscorecompose.R

/**
 * Created by jrzeznicki on 20/01/2023.
 */

sealed class FilterChip(textId: Int) {
    sealed class Standings(val textId: Int) : FilterChip(textId) {
        object All : Standings(R.string.all)
        object Home : Standings(R.string.home)
        object Away : Standings(R.string.away)
    }

    sealed class Fixtures(val textId: Int) : FilterChip(textId) {
        object Played : Fixtures(R.string.played)
        object Upcoming : Fixtures(R.string.upcoming)
        object Live : Fixtures(R.string.live)
    }

    sealed class Lineups(val textId: Int) : FilterChip(textId) {
        object HomeTeam : Lineups(R.string.home)
        object AwayTeam : Lineups(R.string.away)
    }
}