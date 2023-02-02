package com.kuba.flashscorecompose.teamdetails.container.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsError
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsUiState
import com.kuba.flashscorecompose.teamdetails.container.viewmodel.TeamDetailsViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FlashScoreSnackbarHost
import com.kuba.flashscorecompose.ui.component.HeaderDetailsWithImage
import com.kuba.flashscorecompose.ui.component.tabs.ScrollableTabs
import com.kuba.flashscorecompose.ui.component.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.tabs.TabsContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 27/01/2023.
 */

private const val SETUP_TEAM_DETAILS_KEY = "SETUP_TEAM_DETAILS_KEY"

@Destination
@Composable
fun TeamDetailsRoute(
    teamId: Int,
    leagueId: Int,
    season: Int,
    viewModel: TeamDetailsViewModel = getViewModel { parametersOf(teamId) },
    navigator: DestinationsNavigator
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_TEAM_DETAILS_KEY) { viewModel.setup() }
    TeamDetailsScreen(
        uiState = uiState,
        teamId = teamId,
        leagueId = leagueId,
        season = season,
        navigator = navigator,
        onErrorClear = { viewModel.cleanError() },
        snackbarHostState = snackbarHostState
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: TeamDetailsUiState,
    teamId: Int,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator) },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            when (uiState) {
                is TeamDetailsUiState.HasData -> {
                    HeaderDetailsWithImage(uiState.team.name, uiState.team.logo)
                    TeamDetailsTabs(uiState.team, teamId, leagueId, season, navigator)
                }
                is TeamDetailsUiState.NoData -> {
                    EmptyState(
                        modifier = Modifier.fillMaxWidth(),
                        textId = R.string.no_team_details
                    )
                }
            }
        }
    }
    ErrorSnackbar(
        uiState = uiState,
        onErrorClear = onErrorClear,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator) {
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
        title = {}
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TeamDetailsTabs(
    team: Team,
    teamId: Int,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    Log.d("TEST_LOG", "Team in teamdetails = $team")
    val tabs = listOf(
        TabItem.TeamDetails.Information(teamId, leagueId, season, navigator),
        TabItem.TeamDetails.Players(team, season, navigator),
        TabItem.TeamDetails.Fixtures(teamId, season, navigator),
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

@Composable
private fun ErrorSnackbar(
    uiState: TeamDetailsUiState,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (uiState.error) {
        is TeamDetailsError.NoError -> {}
        is TeamDetailsError.EmptyTeamDetails -> {
            val errorMessageText = stringResource(id = R.string.no_team_details)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
    }
}
