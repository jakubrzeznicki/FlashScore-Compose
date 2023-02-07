package com.kuba.flashscorecompose.leaguedetails.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.StandingsDetailsRouteDestination
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsError
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsUiState
import com.kuba.flashscorecompose.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.snackbar.FlashScoreSnackbarHost
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

@Destination
@Composable
fun LeagueDetailsRoute(
    league: League,
    navigator: DestinationsNavigator,
    viewModel: LeagueDetailsViewModel = getViewModel { parametersOf(league) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_LEAGUE_DETAILS_KEY) { viewModel.setup() }
    LeagueDetailsScreen(
        uiState = uiState,
        league = league,
        snackbarHostState = snackbarHostState,
        navigator = navigator,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it)) },
        onDateClick = { viewModel.changeDate(it) },
        onStandingsClick = { navigator.navigate(StandingsDetailsRouteDestination(league)) },
        onErrorClear = { viewModel.cleanError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDetailsScreen(
    uiState: LeagueDetailsUiState,
    league: League,
    snackbarHostState: SnackbarHostState,
    navigator: DestinationsNavigator,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onStandingsClick: () -> Unit,
    onErrorClear: () -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopBar(
                navigator = navigator,
                league = league
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
                    .padding(horizontal = 16.dp),
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
                    StandingsRow(onStandingClick = onStandingsClick)
                }
            }
        }
    }
    ErrorSnackbar(
        uiState = uiState,
        onRefreshClick = onRefreshClick,
        onErrorClear = onErrorClear,
        snackbarHostState = snackbarHostState
    )
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
                .padding(horizontal = 4.dp)
                .size(24.dp),
            onClick = { calendarState.show() }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun StandingsRow(onStandingClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onStandingClick() }
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp),
            onClick = { onStandingClick() }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.List,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.standings),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        IconButton(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(24.dp),
            onClick = { onStandingClick() }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.ChevronRight,
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
        modifier = Modifier
            .height(58.dp)
            .padding(vertical = 8.dp),
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
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(24.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(league.logo)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_close),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = league.name,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = FlashScoreTypography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        })
}

@Composable
private fun ErrorSnackbar(
    uiState: LeagueDetailsUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (val error = uiState.error) {
        is LeagueDetailsError.NoError -> {}
        is LeagueDetailsError.RemoteError -> {
            val errorMessageText =
                remember(uiState) { error.responseStatus.statusMessage.orEmpty() }
            val retryMessageText = stringResource(id = R.string.retry)
            val onRefreshPostStates by rememberUpdatedState(onRefreshClick)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText, retryMessageText) {
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = errorMessageText,
                    actionLabel = retryMessageText
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    onRefreshPostStates()
                }
                onErrorDismissState()
            }
        }
    }
}