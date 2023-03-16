package com.example.fixturedetails.container.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.fixturedetails.R
import com.example.fixturedetails.container.model.FixtureDetailsUiState
import com.example.fixturedetails.container.viewmodel.FixtureDetailsViewModel
import com.example.fixturedetails.headtohead.screen.HeadToHeadScreen
import com.example.fixturedetails.lineup.screen.LineupScreen
import com.example.fixturedetails.statistics.screen.StatisticsScreen
import com.example.model.fixture.FixtureItem
import com.example.model.team.Team
import com.example.ui.composables.CenterAppTopBar
import com.example.ui.composables.EmptyState
import com.example.ui.composables.FullScreenLoading
import com.example.ui.composables.LoadingContent
import com.example.ui.composables.tabs.TabItem
import com.example.ui.composables.tabs.Tabs
import com.example.ui.composables.tabs.TabsContent
import com.example.ui.theme.FlashScoreTypography
import com.google.accompanist.pager.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val SETUP_FIXTURE_DETAILS_KEY = "SETUP_FIXTURE_DETAILS_KEY"

//@Destination(
//    deepLinks = [
//        DeepLink(uriPattern = "$MY_URI/$FIXTURE_ID_ARGS={fixtureId}")
//    ]
//)
@Composable
fun FixtureDetailsRoute(
    fixtureId: Int,
    navigator: DestinationsNavigator,
    viewModel: FixtureDetailsViewModel = getViewModel { parametersOf(fixtureId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = SETUP_FIXTURE_DETAILS_KEY) { viewModel.setup() }
    FixtureDetailsScreen(
        uiState = uiState,
        navigator = navigator,
        onRefreshClick = { viewModel.refreshFixtureItem() },
        onTeamClick = { team, leagueId, season ->
            // navigator.navigate(TeamDetailsRouteDestination(team, leagueId, season))
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FixtureDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: FixtureDetailsUiState,
    navigator: DestinationsNavigator,
    onRefreshClick: () -> Unit,
    onTeamClick: (Team, Int, Int) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                navigator = navigator,
                round = when (uiState) {
                    is FixtureDetailsUiState.HasData -> uiState.fixtureItem.league.round
                    else -> ""
                }
            )
        }
    ) {
        LoadingContent(
            modifier = Modifier.padding(top = 36.dp),
            empty = when (uiState) {
                is FixtureDetailsUiState.HasData -> false
                else -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            Column(Modifier.fillMaxSize()) {
                when (uiState) {
                    is FixtureDetailsUiState.HasData -> {
                        HeaderMatchInfo(uiState.fixtureItem, onTeamClick)
                        FixtureDetailsTabs(uiState.fixtureItem, navigator)
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator, round: String) {
    CenterAppTopBar(
        modifier = Modifier
            .height(58.dp)
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
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = round,
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
                placeholder = painterResource(id = com.example.ui.R.drawable.ic_close),
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
            text = fixtureItem.fixture.formattedDate,
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
    fixtureItem: FixtureItem,
    navigator: DestinationsNavigator
) {
    val tabs = listOf(
        TabItem.FixtureDetails.Statistics {
            StatisticsScreen(
                fixtureItem.id,
                fixtureItem.league.id,
                fixtureItem.league.round,
                fixtureItem.season,
                navigator
            )
        },
        TabItem.FixtureDetails.LineUp {
            LineupScreen(
                fixtureItem.id,
                fixtureItem.league.id,
                fixtureItem.season,
                navigator
            )
        },
        TabItem.FixtureDetails.HeadToHead {
            HeadToHeadScreen(
                fixtureItem.homeTeam,
                fixtureItem.awayTeam,
                fixtureItem.season,
                navigator
            )
        }
    )
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Tabs(tabs = tabs, pagerState = pagerState, coroutineScope)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}
