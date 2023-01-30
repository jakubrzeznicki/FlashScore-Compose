package com.kuba.flashscorecompose.teamdetails.container.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsUiState
import com.kuba.flashscorecompose.teamdetails.container.viewmodel.TeamDetailsViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
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

@Destination(route = "home/teamdetails")
@Composable
fun TeamDetailsRoute(
    teamId: Int,
    leagueId: Int,
    season: Int,
    viewModel: TeamDetailsViewModel = getViewModel { parametersOf(teamId) },
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_TEAM_DETAILS_KEY) { viewModel.setup() }
    TeamDetailsScreen(
        uiState = uiState,
        teamId = teamId,
        leagueId = leagueId,
        season = season,
        navigator = navigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: TeamDetailsUiState,
    teamId: Int,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TeamHeader(uiState.team)
            TeamDetailsTabs(uiState, teamId, leagueId, season, navigator)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator) {
    CenterAppTopBar(
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

@Composable
private fun TeamHeader(team: Team?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.surface)
                .border(
                    width = 2.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .size(112.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(90.dp)
                    .padding(12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(team?.logo)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = team?.name.orEmpty(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = team?.country.orEmpty(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TeamDetailsTabs(
    uiState: TeamDetailsUiState,
    teamId: Int,
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator
) {
    val tabs = listOf(
        TabItem.TeamDetails.Information(teamId, leagueId, navigator),
        TabItem.TeamDetails.Players(teamId, season, navigator),
        TabItem.TeamDetails.Fixtures(teamId, season, navigator),
        TabItem.TeamDetails.Injuries(uiState.team, navigator),
        TabItem.TeamDetails.Transfers(uiState.team, navigator)
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ScrollableTabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}
