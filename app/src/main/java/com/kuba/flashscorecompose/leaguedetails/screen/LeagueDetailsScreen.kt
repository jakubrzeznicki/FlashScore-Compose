package com.kuba.flashscorecompose.leaguedetails.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.StandingsDetailsRouteDestination
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsUiState
import com.kuba.flashscorecompose.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */

private const val SETUP_LEAGUE_DETAILS_KEY = "SETUP_LEAGUE_DETAILS_KEY"

@Destination(route = "home/leaguedetails")
@Composable
fun LeagueDetailsRoute(
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: LeagueDetailsViewModel = getViewModel { parametersOf(leagueId, season) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_LEAGUE_DETAILS_KEY) { viewModel.setup() }
    LeagueDetailsScreen(
        leagueId,
        season,
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        navigator = navigator,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.id)) },
        onDateClick = { viewModel.changeDate(it) },
        onStandingsClick = { leagueIdParam, seasonParam ->
            navigator.navigate(StandingsDetailsRouteDestination(leagueIdParam, seasonParam))
        },
        onErrorClick = { viewModel.cleanError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDetailsScreen(
    leagueId: Int,
    season: Int,
    uiState: LeagueDetailsUiState,
    snackbarHostState: SnackbarHostState,
    navigator: DestinationsNavigator,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onStandingsClick: (Int, Int) -> Unit,
    onErrorClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopBar(
                navigator = navigator,
                league = uiState.league
            )
        },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LoadingContent(
            modifier = Modifier.padding(paddingValues),
            empty = when (uiState) {
                is LeagueDetailsUiState.HasData -> false
                is LeagueDetailsUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                state = scrollState
            ) {
                item {
                    DateRow(localDate = uiState.date, onDateClick = onDateClick)
                    Spacer(modifier = Modifier.size(24.dp))
                }
                when (uiState) {
                    is LeagueDetailsUiState.HasData -> {
                        items(items = uiState.fixtureItems) {
                            FixtureCard(fixtureItem = it, onFixtureClick = onFixtureClick)
                        }
                    }
                    is LeagueDetailsUiState.NoData -> {
                        item {
                            EmptyState(
                                modifier = Modifier.fillMaxWidth(),
                                textId = R.string.no_fixtures
                            )
                        }
                    }
                }
                item {
                    StandingsRow(
                        leagueId = leagueId,
                        season = season,
                        onStandingClick = onStandingsClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRow(localDate: LocalDate, onDateClick: (LocalDate) -> Unit) {
    val calendarState = rememberSheetState()
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date(selectedDate = localDate) { date -> onDateClick(date) }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = localDate.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        IconButton(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(32.dp),
            onClick = { calendarState.show() }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun StandingsRow(leagueId: Int, season: Int, onStandingClick: (Int, Int) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onStandingClick(leagueId, season) }
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(32.dp),
            onClick = { }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.standing),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        IconButton(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(32.dp),
            onClick = { }
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigator: DestinationsNavigator, league: League) {
    CenterAppTopBar(
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(start = 12.dp)
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
                modifier = Modifier.matchParentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(league.logo)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = league.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = FlashScoreTypography.headlineSmall
                )
            }
        })
}