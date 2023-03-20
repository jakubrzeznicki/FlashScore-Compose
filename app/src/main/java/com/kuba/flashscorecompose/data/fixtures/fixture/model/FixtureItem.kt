package com.kuba.flashscorecompose.data.fixtures.fixture.model

import android.os.Parcelable
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.team.information.model.Team
import kotlinx.parcelize.Parcelize

@Parcelize
data class FixtureItem(
    val id: Int,
    val season: Int,
    val round: String,
    val h2h: String,
    val fixture: FixtureInfo,
    val goals: Goals,
    val league: League,
    val score: Score,
    val homeTeam: Team,
    val awayTeam: Team
) : Parcelable {
    companion object {
        val EMPTY_FIXTURE_ITEM = FixtureItem(
            0,
            0,
            "",
            "",
            FixtureInfo.EMPTY_FIXTURE_INFO,
            Goals.EMPTY_GOALS,
            League.EMPTY_LEAGUE,
            Score.EMPTY_SCORE,
            Team.EMPTY_TEAM,
            Team.EMPTY_TEAM
        )
    }
}
