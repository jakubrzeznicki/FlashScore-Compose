package com.kuba.flashscorecompose.data.fixtures.fixture.model

data class Teams(val away: Team, val home: Team) {
    companion object {
        val EMPTY_TEAMS = Teams(Team.EMPTY_TEAM, Team.EMPTY_TEAM)
    }
}