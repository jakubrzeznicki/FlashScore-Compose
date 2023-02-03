package com.kuba.flashscorecompose.teamdetails.container.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.tabs.ScrollableTabs
import com.kuba.flashscorecompose.ui.component.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.tabs.TabsContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 27/01/2023.
 */

@Destination
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
                onClick = { navigator.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
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
        TabItem.TeamDetails.Information(team, leagueId, season, navigator),
        TabItem.TeamDetails.Players(team, season, navigator),
        TabItem.TeamDetails.Fixtures(team.id, season, navigator),
        TabItem.TeamDetails.Injuries(team, navigator),
        TabItem.TeamDetails.Transfers(team, navigator)
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ScrollableTabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}