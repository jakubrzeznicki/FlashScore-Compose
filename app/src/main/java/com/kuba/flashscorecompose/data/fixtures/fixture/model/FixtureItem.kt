package com.kuba.flashscorecompose.data.fixtures.fixture.model

import com.kuba.flashscorecompose.data.league.model.League

data class FixtureItem(
    val id: Int,
    val leagueId: Int,
    val season: Int,
    val round: String,
    val h2h: String,
    val fixture: FixtureInfo,
    val goals: Goals,
    val league: League,
    val score: Score,
    val homeTeam: Team,
    val awayTeam: Team
)