package com.kuba.flashscorecompose.fixturedetails.tabs

import androidx.compose.runtime.Composable
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.fixturedetails.headtohead.screen.HeadToHeadScreen
import com.kuba.flashscorecompose.fixturedetails.lineup.screen.LineupScreen
import com.kuba.flashscorecompose.fixturedetails.statistics.screen.StatisticsScreen

/**
 * Created by jrzeznicki on 23/12/2022.
 */
typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var title: String, var screen: ComposableFun) {
    class MatchDetail(fixtureId: Int, leagueId: Int, round: String) :
        TabItem(
            R.drawable.ic_profile,
            "Match Detail",
            { StatisticsScreen(fixtureId, leagueId, round) })

    class LineUp(fixtureId: Int) :
        TabItem(R.drawable.ic_standings, "Line Up", { LineupScreen(fixtureId) })

    class HeadToHead(homeTeam: Team, awayTeam: Team, season: Int) :
        TabItem(R.drawable.ic_explore, "H2H", { HeadToHeadScreen(homeTeam, awayTeam, season) })
}