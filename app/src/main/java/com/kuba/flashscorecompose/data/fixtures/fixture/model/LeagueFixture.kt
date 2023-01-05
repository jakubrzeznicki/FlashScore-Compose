package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class LeagueFixture(
    val country: String,
    val flag: String,
    val id: Int,
    val logo: String,
    val name: String,
    val round: String,
    val season: Int
) {
    companion object {
        val EMPTY_LEAGUE_FIXTURE = LeagueFixture("", "", 0, "", "", "", 0)
    }
}