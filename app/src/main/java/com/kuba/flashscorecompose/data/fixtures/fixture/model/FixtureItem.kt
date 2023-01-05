package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class FixtureItem(
    val id: Int,
    val leagueId: Int,
    val season: Int,
    val round: String,
    val h2h: String,
    val fixture: FixtureInfo,
    val goals: Goals,
    val league: LeagueFixture,
    val score: Score,
    val teams: Teams
)