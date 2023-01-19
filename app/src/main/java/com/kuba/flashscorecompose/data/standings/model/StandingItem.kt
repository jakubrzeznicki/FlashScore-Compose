package com.kuba.flashscorecompose.data.standings.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team

data class StandingItem(
    val all: InformationStanding,
    val away: InformationStanding,
    val home: InformationStanding,
    val description: String,
    val form: String,
    val goalsDiff: Int,
    val group: String,
    val points: Int,
    val rank: Int,
    val status: String,
    val team: Team,
    val update: String
) {
    companion object {
        val EMPTY_STANDING_ITEM = StandingItem(
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            "",
            "",
            0,
            "",
            0,
            0,
            "",
            Team.EMPTY_TEAM,
            ""
        )
    }
}