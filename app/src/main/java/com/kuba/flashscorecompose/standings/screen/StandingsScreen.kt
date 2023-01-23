package com.kuba.flashscorecompose.standings.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.standings.model.GoalsStanding
import com.kuba.flashscorecompose.data.standings.model.InformationStanding
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standings.model.StandingsUiState
import com.kuba.flashscorecompose.standings.viewmodel.StandingsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.destinations.StandingsDetailsRouteDestination
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.theme.GreyDark
import com.kuba.flashscorecompose.ui.theme.GreyLight
import com.kuba.flashscorecompose.ui.theme.GreyTextDark
import com.kuba.flashscorecompose.ui.theme.White

/**
 * Created by jrzeznicki on 23/12/2022.
 */

private const val SETUP_STANDINGS_KEY = "SETUP_STANDINGS_KEY"

@Destination(route = "standings")
@Composable
fun StandingsRoute(
    navigator: DestinationsNavigator,
    viewModel: StandingsViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_STANDINGS_KEY) { viewModel.setup() }
    StandingsScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onCountryClick = { country, isSelected ->
            viewModel.updateSelectedCountry(country, isSelected)
        },
        onStandingsClick = {
            navigator.navigate(
                StandingsDetailsRouteDestination(
                    leagueId = it.leagueId,
                    season = it.season
                )
            )
        },
        onErrorClear = { viewModel.cleanError() },
        scaffoldState = scaffoldState,
        onStandingsQueryChanged = { viewModel.updateStandingsQuery(it) }
    )
}

@Composable
fun StandingsScreen(
    modifier: Modifier = Modifier,
    uiState: StandingsUiState,
    onRefreshClick: () -> Unit,
    onCountryClick: (Country, Boolean) -> Unit,
    onStandingsClick: (Standing) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState,
    onStandingsQueryChanged: (String) -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = { TopBar() },
        scaffoldState = scaffoldState,
        snackbarHost = { FlashScoreSnackbarHost(hostState = it) }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            empty = when (uiState) {
                is StandingsUiState.HasData -> false
                is StandingsUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                state = scrollState
            ) {
                item {
                    SimpleSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = GreyLight)
                            .border(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(width = 2.dp, color = GreyDark),
                            ),
                        label = stringResource(id = R.string.search_standings),
                        query = uiState.standingsQuery,
                        onQueryChange = onStandingsQueryChanged,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = GreyTextDark
                            )
                        }
                    )
                    Spacer(modifier = Modifier.size(28.dp))
                }
                when (uiState) {
                    is StandingsUiState.HasData -> {
                        item {
                            CountriesWidget(
                                countries = uiState.countries,
                                onCountryClick = onCountryClick,
                                selectedItem = uiState.selectedCountry
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                        items(items = uiState.standings) {
                            StandingWithLeagueItem(it, onStandingsClick)
                        }
                    }
                    is StandingsUiState.NoData -> {
                        item {
                            EmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                iconId = R.drawable.ic_close,
                                contentDescriptionId = R.string.load_data_from_network,
                                textId = R.string.no_standings,
                                onRefreshClick = onRefreshClick
                            )
                        }
                    }
                }
            }
        }
    }
    ErrorSnackbar(
        uiState = uiState,
        onRefreshClick = onRefreshClick,
        onErrorClear = onErrorClear,
        scaffoldState = scaffoldState
    )
}

@Composable
fun StandingWithLeagueItem(
    standing: Standing,
    onStandingsClick: (Standing) -> Unit
) {
    Column(
        Modifier
            .clickable { onStandingsClick(standing) }
            .padding(bottom = 16.dp)
    ) {
        LeagueHeader(league = standing.league, onLeagueClick = {})
        StandingCard(standing.standingItems)
    }
}

@Composable
fun StandingCard(standingItems: List<StandingItem>) {
    Card(backgroundColor = GreyLight, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            StandingHeaderRow()
            Divider(
                color = GreyDark,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.End)
            )
            standingItems.take(4).forEach { standingItem ->
                StandingElementRow(standingItem)
                Divider(
                    color = GreyDark,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun TopBar() {
    AppTopBar(title = { Text(text = stringResource(id = R.string.standings), color = Color.White) })
}

@Composable
private fun StandingHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(6f),
            text = stringResource(id = R.string.team),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.played),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.wins),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.loses),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.draws),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.goals_diff),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.points),
            fontSize = 12.sp,
            color = White
        )
    }
}

@Composable
private fun StandingElementRow(standingItem: StandingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .size(24.dp)
                .padding(end = 4.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(standingItem.team.logo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier
                .weight(5f)
                .padding(end = 4.dp),
            text = standingItem.team.name,
            fontSize = 12.sp,
            color = White,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.played.toString(),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.win.toString(),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.lose.toString(),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.draw.toString(),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.goalsDiff.toString(),
            fontSize = 12.sp,
            color = White
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.points.toString(),
            fontSize = 12.sp,
            color = White
        )
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: StandingsUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState
) {
    when (val error = uiState.error) {
        is StandingsError.NoError -> {}
        is StandingsError.EmptyLeague -> {
            val errorMessageText = stringResource(id = R.string.empty_leagues)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText, scaffoldState) {
                scaffoldState.snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
        is StandingsError.RemoteError -> {
            val errorMessageText =
                remember(uiState) { error.responseStatus.statusMessage.orEmpty() }
            val retryMessageText = stringResource(id = R.string.retry)
            val onRefreshPostStates by rememberUpdatedState(onRefreshClick)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText, retryMessageText, scaffoldState) {
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
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

@Preview
@Composable
fun Test() {
    val standings = listOf(
        StandingItem(
            InformationStanding(0, GoalsStanding.EMPTY_GOALS_STANDING, 0, 0, 0),
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            "sdsd",
            "sdf",
            2,
            "dfdf",
            1,
            1,
            "dfd",
            Team.EMPTY_TEAM,
            "dfsdf"
        ),
        StandingItem(
            InformationStanding(0, GoalsStanding.EMPTY_GOALS_STANDING, 0, 0, 0),
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            "sdsd",
            "sdf",
            2,
            "dfdf",
            1,
            1,
            "dfd",
            Team.EMPTY_TEAM,
            "dfsdf"
        ),
        StandingItem(
            InformationStanding(0, GoalsStanding.EMPTY_GOALS_STANDING, 0, 0, 0),
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            InformationStanding.EMPTY_INFORMATION_STANDING,
            "sdsd",
            "sdf",
            2,
            "dfdf",
            1,
            1,
            "dfd",
            Team.EMPTY_TEAM,
            "dfsdf"
        )
    )
    Card(backgroundColor = GreyLight, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            StandingHeaderRow()
            Spacer(modifier = Modifier.size(16.dp))
            standings.forEach { standingItem ->
                StandingElementRow(standingItem)
                Divider(
                    color = GreyDark,
                    thickness = 3.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.End),
                )
            }
        }
    }
}

@Preview
@Composable
fun TextSimpleTextField() {
    SimpleSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color = GreyLight)
            .border(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 2.dp, color = GreyDark),
            ),
        label = stringResource(id = R.string.search_standings),
        query = "dd",
        onQueryChange = { },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = GreyTextDark
            )
        }
    )
}