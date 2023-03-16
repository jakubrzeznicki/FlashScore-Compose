package com.example.teamdetails.container

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.team.Team
import com.example.teamdetails.fixturesteam.screen.FixturesTeamScreen
import com.example.teamdetails.informations.screen.TeamInformationsScreen
import com.example.teamdetails.players.screen.PlayersScreen
import com.example.ui.composables.CenterAppTopBar
import com.example.ui.composables.HeaderDetails
import com.example.ui.composables.tabs.ScrollableTabs
import com.example.ui.composables.tabs.TabItem
import com.example.ui.composables.tabs.TabsContent
import com.example.ui.theme.FlashScoreTypography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 27/01/2023.
 */

//@Destination
@Composable
fun TeamDetailsRoute(
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    TeamDetailsScreen(
        team = team,
        leagueId = leagueId,
        season = season,
        navigator = navigator
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    modifier: Modifier = Modifier,
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator = navigator, title = team.name) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp)
        ) {
            HeaderDetails(team.logo)
            TeamDetailsTabs(team, leagueId, season, navigator)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator, title: String) {
    CenterAppTopBar(
        modifier = Modifier
            .height(42.dp)
            .padding(vertical = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { navigator.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamDetailsTabs(
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    val tabs = listOf(
        TabItem.TeamDetails.Information {
            TeamInformationsScreen(
                team,
                leagueId,
                season,
                navigator
            )
        },
        TabItem.TeamDetails.Players {
            PlayersScreen(
                team,
                season = season,
                navigator = navigator
            )
        },
        TabItem.TeamDetails.Fixtures {
            FixturesTeamScreen(
                teamId = team.id,
                season = season,
                navigator = navigator
            )
        },
        TabItem.TeamDetails.Injuries {},
        TabItem.TeamDetails.Transfers {}
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ScrollableTabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}
