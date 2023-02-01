package com.kuba.flashscorecompose.standingsdetails.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.destinations.LeagueDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.TeamDetailsRouteDestination
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsError
import com.kuba.flashscorecompose.standingsdetails.model.StandingsDetailsUiState
import com.kuba.flashscorecompose.standingsdetails.viewmodel.StandingsDetailsViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 19/01/2023.
 */

private const val SETUP_STANDINGS_DETAILS_KEY = "SETUP_STANDINGS_DETAILS_KEY"
private const val HASZTAG = "#"

@Destination
@Composable
fun StandingsDetailsRoute(
    leagueId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: StandingsDetailsViewModel = getViewModel { parametersOf(leagueId, season) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_STANDINGS_DETAILS_KEY) { viewModel.setup() }
    StandingsDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        navigator = navigator,
        onTeamClick = {
            navigator.navigate(TeamDetailsRouteDestination(it.id, leagueId, season))
        },
        onLeagueClick = { navigator.navigate(LeagueDetailsRouteDestination(leagueId, season)) },
        onRefreshClick = { viewModel.refresh() },
        onErrorClear = { viewModel.cleanError() },
        onStandingsFilterClick = { viewModel.filterStandings(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandingsDetailsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: StandingsDetailsUiState,
    navigator: DestinationsNavigator,
    onTeamClick: (Team) -> Unit,
    onLeagueClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    onStandingsFilterClick: (FilterChip.Standings) -> Unit
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                navigator = navigator,
                league = when (uiState) {
                    is StandingsDetailsUiState.HasData -> uiState.league
                    else -> League.EMPTY_LEAGUE
                }
            )
        },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            empty = when (uiState) {
                is StandingsDetailsUiState.HasData -> false
                else -> uiState.isLoading
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
                when (uiState) {
                    is StandingsDetailsUiState.HasData -> {
                        item {
                            LeagueHeader(uiState.league, onLeagueClick)
                        }
                        item {
                            StandingFilterChips(uiState.standingFilterChip, onStandingsFilterClick)
                        }
                        item {
                            StandingHeaderRow()
                            Divider(
                                color = MaterialTheme.colorScheme.inverseSurface,
                                thickness = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )
                        }
                        items(items = uiState.standingsItems) {
                            StandingElementRow(it, onTeamClick)
                        }
                    }
                    is StandingsDetailsUiState.NoData -> {
                        item {
                            EmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                textId = R.string.no_standings
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(8.dp),
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            }
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
        snackbarHostState = snackbarHostState
    )
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
                    contentDescription = "",
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
                        .data(league.countryFlag)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = league.countryName,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = FlashScoreTypography.headlineSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        })
}

@Composable
private fun LeagueHeader(league: League, onLeagueClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onLeagueClick() },
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
                    .data(league.logo)
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
            text = league.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StandingFilterChips(
    standingFilerChip: FilterChip.Standings,
    onStandingsFilterClick: (FilterChip.Standings) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        FilterStandingChip(
            standingFilterChip = FilterChip.Standings.All,
            isSelected = standingFilerChip is FilterChip.Standings.All,
            onStateChanged = onStandingsFilterClick,
            icon = Icons.Filled.Subject
        )
        FilterStandingChip(
            standingFilterChip = FilterChip.Standings.Home,
            isSelected = standingFilerChip is FilterChip.Standings.Home,
            onStateChanged = onStandingsFilterClick,
            icon = Icons.Filled.Home
        )
        FilterStandingChip(
            standingFilterChip = FilterChip.Standings.Away,
            isSelected = standingFilerChip is FilterChip.Standings.Away,
            onStateChanged = onStandingsFilterClick,
            icon = Icons.Filled.FlightTakeoff

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterStandingChip(
    standingFilterChip: FilterChip.Standings,
    isSelected: Boolean,
    onStateChanged: (FilterChip.Standings) -> Unit,
    icon: ImageVector
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            iconColor = MaterialTheme.colorScheme.onSecondary,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
            selectedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        label = {
            Text(
                text = stringResource(id = standingFilterChip.textId),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        },
        shape = RoundedCornerShape(50),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = icon,
                contentDescription = null
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.inverseSurface
        ),
        onClick = { onStateChanged(standingFilterChip) }
    )
}

@Composable
private fun StandingHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = HASZTAG,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(6f),
            text = stringResource(id = R.string.team),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.wins),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.loses),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.draws),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.goals_diff),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.points),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun StandingElementRow(
    standingItem: StandingItem,
    onTeamClick: (Team) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onTeamClick(standingItem.team) }
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = colorResource(id = standingItem.colorId),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.rank.toString(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .size(28.dp)
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
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.win.toString(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.lose.toString(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.selectedInformationStanding.draw.toString(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.goalsDiff.toString(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.points.toString(),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: StandingsDetailsUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (val error = uiState.error) {
        is StandingsDetailsError.NoError -> {}
        is StandingsDetailsError.EmptyStanding -> {
            val errorMessageText = stringResource(id = R.string.empty_leagues)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
        is StandingsDetailsError.RemoteError -> {
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