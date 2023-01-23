package com.kuba.flashscorecompose.data.standings.model

import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team

data class StandingItem(
    val all: InformationStanding,
    val away: InformationStanding,
    val home: InformationStanding,
    val selectedInformationStanding: InformationStanding,
    val description: String,
    val form: String,
    val goalsDiff: Int,
    val group: String,
    val points: Int,
    val rank: Int,
    val status: String,
    val team: Team,
    val update: String,
    val colorId: Int = R.color.lightGrey
) {
    companion object {
        val EMPTY_STANDING_ITEM = StandingItem(
            InformationStanding.EMPTY_INFORMATION_STANDING,
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
            "",
            R.color.lightGrey
        )
    }
}