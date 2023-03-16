package com.example.model.standings

import com.example.model.league.League


data class Standing(
    val league: League,
    val leagueId: Int,
    val season: Int,
    val standingItems: List<StandingItem>
) {
    companion object {
        val EMPTY_STANDING = Standing(League.EMPTY_LEAGUE, 0, 0, listOf())
    }
}
