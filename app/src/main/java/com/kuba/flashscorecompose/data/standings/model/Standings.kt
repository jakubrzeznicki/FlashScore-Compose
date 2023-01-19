package com.kuba.flashscorecompose.data.standings.model

import com.kuba.flashscorecompose.data.league.model.League

data class Standings(
    val league: League,
    val leagueId: Int,
    val season: Int,
    val standings: List<StandingItem>
) {
    companion object {
        val EMPTY_STANDINGS = Standings(League.EMPTY_LEAGUE, 0, 0, listOf())
    }
}