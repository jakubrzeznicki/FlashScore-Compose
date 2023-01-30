package com.kuba.flashscorecompose.ui.component.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.fixturedetails.headtohead.screen.HeadToHeadScreen
import com.kuba.flashscorecompose.fixturedetails.lineup.screen.LineupScreen
import com.kuba.flashscorecompose.fixturedetails.statistics.screen.StatisticsScreen
import com.kuba.flashscorecompose.teamdetails.fixturesteam.screen.FixturesTeamScreen
import com.kuba.flashscorecompose.teamdetails.informations.screen.TeamInformationsScreen
import com.kuba.flashscorecompose.teamdetails.players.screen.PlayersScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 23/12/2022.
 */
typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: ImageVector, var titleId: Int, var screen: ComposableFun) {

    sealed interface FixtureDetails {
        class Statistics(
            fixtureId: Int,
            leagueId: Int,
            round: String,
            season: Int,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.QueryStats,
            R.string.statistics,
            { StatisticsScreen(fixtureId, leagueId, round, season, navigator) })

        class LineUp(fixtureId: Int, navigator: DestinationsNavigator) :
            TabItem(
                Icons.Default.Square,
                R.string.lineups,
                { LineupScreen(fixtureId, navigator) })

        class HeadToHead(
            homeTeam: Team,
            awayTeam: Team,
            season: Int,
            fixtureId: Int,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.Headset,
            R.string.head_to_head,
            { HeadToHeadScreen(homeTeam, awayTeam, season, fixtureId, navigator) })
    }

    sealed interface TeamDetails {

        class Information(
            teamId: Int,
            leagueId: Int,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.Info,
            R.string.informations,
            { TeamInformationsScreen(teamId, leagueId, navigator) }
        )

        class Players(
            teamId: Int,
            season: Int,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.Info,
            R.string.players,
            { PlayersScreen(teamId = teamId, season = season, navigator = navigator) }
        )

        class Fixtures(
            teamId: Int,
            season: Int,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.FiberDvr,
            R.string.fixtures,
            { FixturesTeamScreen(teamId = teamId, season = season, navigator = navigator) }
        )

        class Injuries(
            team: Team?,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.PersonalInjury,
            R.string.injuries,
            {}
        )

        class Transfers(
            team: Team?,
            navigator: DestinationsNavigator
        ) : TabItem(
            Icons.Default.Transform,
            R.string.transfers,
            {}
        )
    }
}