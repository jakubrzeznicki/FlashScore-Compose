package com.example.teamdetails.container

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
import com.example.teamdetails.navigation.TeamDetailsNavigator
import com.example.teamdetails.players.screen.PlayersScreen
import com.example.ui.composables.CenterAppTopBar
import com.example.ui.composables.HeaderDetails
import com.example.ui.composables.tabs.ScrollableTabs
import com.example.ui.composables.tabs.TabItem
import com.example.ui.composables.tabs.TabsContent
import com.example.ui.theme.FlashScoreTypography
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination

/**
 * Created by jrzeznicki on 27/01/2023.
 */

@Destination
@Composable
fun TeamDetailsRoute(
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: TeamDetailsNavigator
) {
    TeamDetailsScreen(
        team = team,
        leagueId = leagueId,
        season = season,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    modifier: Modifier = Modifier,
    team: Team,
    leagueId: Int,
    season: Int,
    navigator: TeamDetailsNavigator
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator = navigator, title = team.name) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HeaderDetails(team.logo)
            TeamDetailsTabs(team, leagueId, season, navigator)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: TeamDetailsNavigator, title: String) {
    CenterAppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(top = 8.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(32.dp),
                onClick = { navigator.navigateUp() }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
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
    navigator: TeamDetailsNavigator
) {
    val tabs = listOf(
        TabItem.TeamDetails.Information {
            TeamInformationsScreen(team, leagueId, season)
        },
        TabItem.TeamDetails.Players {
            PlayersScreen(team, season, navigator)
        },
        TabItem.TeamDetails.Fixtures {
            FixturesTeamScreen(team, season, navigator)
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