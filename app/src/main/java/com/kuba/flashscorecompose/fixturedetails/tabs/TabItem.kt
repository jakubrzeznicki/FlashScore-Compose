package com.kuba.flashscorecompose.fixturedetails.tabs

import androidx.compose.runtime.Composable
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.fixturedetails.headtohead.screen.HeadToHeadScreen
import com.kuba.flashscorecompose.fixturedetails.lineup.screen.LineupScreen
import com.kuba.flashscorecompose.fixturedetails.statistics.screen.StatisticsScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 23/12/2022.
 */
typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int, var titleId: Int, var screen: ComposableFun) {
    class Statistics(
        fixtureId: Int,
        leagueId: Int,
        round: String,
        season: Int,
        navigator: DestinationsNavigator
    ) : TabItem(
        R.drawable.ic_profile,
        R.string.statistics,
        { StatisticsScreen(fixtureId, leagueId, round, season, navigator) })

    class LineUp(fixtureId: Int, navigator: DestinationsNavigator) :
        TabItem(R.drawable.ic_standings, R.string.lineups, { LineupScreen(fixtureId, navigator) })

    class HeadToHead(
        homeTeam: Team,
        awayTeam: Team,
        season: Int,
        fixtureId: Int,
        navigator: DestinationsNavigator
    ) : TabItem(
        R.drawable.ic_explore,
        R.string.head_to_head,
        { HeadToHeadScreen(homeTeam, awayTeam, season, fixtureId, navigator) })
}