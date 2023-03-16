package com.example.leaguedetails.screen

import android.content.Context
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.fixturedetails.R
import com.example.leaguedetails.model.LeagueDetailsUiState
import com.example.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.example.model.fixture.FixtureItemWrapper
import com.example.model.league.League
import com.example.ui.composables.*
import com.example.ui.theme.FlashScoreTypography
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */

private const val SETUP_LEAGUE_DETAILS_KEY = "SETUP_LEAGUE_DETAILS_KEY"

//@Destination
@Composable
fun LeagueDetailsRoute(
    league: League,
    navigator: DestinationsNavigator,
    viewModel: LeagueDetailsViewModel = getViewModel { parametersOf(league) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_LEAGUE_DETAILS_KEY) { viewModel.setup() }
    LeagueDetailsScreen(
        uiState = uiState,
        league = league,
        navigator = navigator,
        context = context,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = {
            //navigator.navigate(FixtureDetailsRouteDestination(it.fixtureItem.id))
        },
        onFixtureFavoriteClick = { viewModel.addFixtureToFavorite(it) },
        onDateClick = { viewModel.changeDate(it) },
        onStandingsClick = {
            //navigator.navigate(StandingsDetailsRouteDestination(league))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDetailsScreen(
    uiState: LeagueDetailsUiState,
    league: League,
    navigator: DestinationsNavigator,
    context: Context,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFixtureFavoriteClick: (FixtureItemWrapper) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onStandingsClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopBar(
                navigator = navigator,
                league = league
            )
        }
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
                        items(items = uiState.fixtureItemWrappers) {
                            FixtureCard(
                                fixtureItemWrapper = it,
                                context = context,
                                onFixtureClick = onFixtureClick,
                                onFavoriteClick = onFixtureFavoriteClick
                            )
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
            color = MaterialTheme.colorScheme.onSecondary
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
            text = stringResource(id = com.example.ui.R.string.standings),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSecondary
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
                AsyncImage(
                    modifier = Modifier
                        .size(24.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .decoderFactory(SvgDecoder.Factory())
                        .data(league.logo)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = com.example.ui.R.drawable.ic_close),
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
        }
    )
}
