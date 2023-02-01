package com.kuba.flashscorecompose.fixturedetails.container.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.pager.*
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.destinations.TeamDetailsRouteDestination
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsUiState
import com.kuba.flashscorecompose.fixturedetails.container.viewmodel.FixtureDetailsViewModel
import com.kuba.flashscorecompose.ui.component.CenterAppTopBar
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FlashScoreSnackbarHost
import com.kuba.flashscorecompose.ui.component.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.tabs.Tabs
import com.kuba.flashscorecompose.ui.component.tabs.TabsContent
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val SETUP_FIXTURE_DETAILS_CONTAINER_KEY = "SETUP_FIXTURE_DETAILS_CONTAINER_KEY"
private const val EMPTY_TITLE = ""

@Destination
@Composable
fun FixtureDetailsRoute(
    fixtureId: Int,
    viewModel: FixtureDetailsViewModel = getViewModel { parametersOf(fixtureId) },
    navigator: DestinationsNavigator,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_FIXTURE_DETAILS_CONTAINER_KEY) { viewModel.setup() }
    FixtureDetailsScreen(
        uiState = uiState,
        navigator = navigator,
        onTeamClick = { team, leagueId, season ->
            navigator.navigate(TeamDetailsRouteDestination(team.id, leagueId, season))
        },
        onErrorClear = { viewModel.cleanError() },
        snackbarHostState = snackbarHostState
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FixtureDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: FixtureDetailsUiState,
    navigator: DestinationsNavigator,
    onTeamClick: (Team, Int, Int) -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator, uiState) },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            when (uiState) {
                is FixtureDetailsUiState.HasData -> {
                    HeaderMatchInfo(uiState.fixtureItem, onTeamClick)
                    FixtureDetailsTabs(uiState, navigator)
                }
                is FixtureDetailsUiState.NoData -> {
                    EmptyState(
                        modifier = Modifier.fillMaxWidth(),
                        textId = R.string.no_fixture_details
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
private fun TopBar(navigator: DestinationsNavigator, uiState: FixtureDetailsUiState) {
    val titleText = when (uiState) {
        is FixtureDetailsUiState.HasData -> uiState.fixtureItem.league.round
        else -> EMPTY_TITLE
    }
    CenterAppTopBar(
        modifier = Modifier
            .height(58.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = titleText,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = FlashScoreTypography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    )
}

@Composable
private fun HeaderMatchInfo(fixtureItem: FixtureItem, onTeamClick: (Team, Int, Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(
            team = fixtureItem.homeTeam,
            leagueId = fixtureItem.league.id,
            season = fixtureItem.season,
            Modifier.weight(2f),
            onTeamClick
        )
        TeamInfoScore(fixtureItem, Modifier.weight(4f))
        TeamInfo(
            team = fixtureItem.awayTeam,
            fixtureItem.league.id,
            season = fixtureItem.season,
            Modifier.weight(2f),
            onTeamClick
        )
    }
}

@Composable
private fun TeamInfo(
    team: Team,
    leagueId: Int,
    season: Int,
    modifier: Modifier,
    onTeamClick: (Team, Int, Int) -> Unit
) {
    Column(
        modifier = modifier.clickable { onTeamClick(team, leagueId, season) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.surface
                )
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                .size(70.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(team.logo)
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
            text = team.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TeamInfoScore(fixtureItem: FixtureItem, modifier: Modifier) {
    Column(
        modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${fixtureItem.goals.home} - ${fixtureItem.goals.away}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = fixtureItem.fixture.status.long,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (fixtureItem.fixture.isLive) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${fixtureItem.fixture.status.elapsed}'",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = fixtureItem.fixture.date,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FixtureDetailsTabs(
    uiState: FixtureDetailsUiState.HasData,
    navigator: DestinationsNavigator
) {
    val tabs = listOf(
        TabItem.FixtureDetails.Statistics(
            uiState.fixtureItem.id,
            uiState.fixtureItem.league.id,
            uiState.fixtureItem.league.round,
            uiState.fixtureItem.season,
            navigator
        ),
        TabItem.FixtureDetails.LineUp(uiState.fixtureItem.id, navigator),
        TabItem.FixtureDetails.HeadToHead(
            uiState.fixtureItem.homeTeam,
            uiState.fixtureItem.awayTeam,
            uiState.fixtureItem.season,
            uiState.fixtureItem.id,
            navigator
        )
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Tabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: FixtureDetailsUiState,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (uiState.error) {
        is FixtureDetailsError.NoError -> {}
        is FixtureDetailsError.EmptyDatabase -> {
            val errorMessageText = stringResource(id = R.string.empty_fixture_details)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
    }
}