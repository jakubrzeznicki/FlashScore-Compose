package com.kuba.flashscorecompose.data.standings.model

import com.kuba.flashscorecompose.data.league.model.League

data class Standing(
    val league: League,
    val leagueId: Int,
    val season: Int,
    val standingItems: List<StandingItem>
) {
    companion object {
        val EMPTY_STANDING = Standing(League.EMPTY_LEAGUE, 0,0, listOf())
    }
}