package com.kuba.flashscorecompose.explore.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.relay.compose.BoxScopeInstanceImpl.align
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.LeaguesListScreenDestination
import com.kuba.flashscorecompose.destinations.PlayerDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.TeamDetailsRouteDestination
import com.kuba.flashscorecompose.explore.model.CoachCountry
import com.kuba.flashscorecompose.explore.model.ExploreError
import com.kuba.flashscorecompose.explore.model.ExploreUiState
import com.kuba.flashscorecompose.explore.model.TeamCountry
import com.kuba.flashscorecompose.explore.viewmodel.ExploreViewModel
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.kuba.flashscorecompose.ui.theme.RedDark
import com.kuba.flashscorecompose.ui.theme.YellowCorn
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 23/12/2022.
 */

private const val SETUP_EXPLORE_KEY = "SETUP_EXPLORE_KEY"

@Destination
@Composable
fun ExploreRoute(
    navigator: DestinationsNavigator,
    viewModel: ExploreViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_EXPLORE_KEY) { viewModel.setup() }
    ExploreScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.id)) },
        onTeamClick = {
            navigator.navigate(
                TeamDetailsRouteDestination(
                    it.team.id,
                    it.team.leagueId,
                    it.team.season
                )
            )
        },
        onPlayerClick = {
            navigator.navigate(
                PlayerDetailsRouteDestination(
                    it.player.id,
                    it.country.flag,
                    it.player.team
                )
            )
        },
        onCountryClick = { LeaguesListScreenDestination(it) },
        onExploreQueryChanged = { viewModel.updateExploreQuery(it) },
        onExploreChipClick = { viewModel.changeExploreView(it as FilterChip.Explore) },
        onErrorClear = { viewModel.cleanError() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    uiState: ExploreUiState,
    snackbarHostState: SnackbarHostState,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onTeamClick: (TeamCountry) -> Unit,
    onPlayerClick: (PlayerCountry) -> Unit,
    onCountryClick: (String) -> Unit,
    onExploreQueryChanged: (String) -> Unit,
    onExploreChipClick: (FilterChip) -> Unit,
    onErrorClear: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            empty = when (uiState) {
                is ExploreUiState.Fixtures.NoData -> uiState.isLoading
                is ExploreUiState.Teams.NoData -> uiState.isLoading
                is ExploreUiState.Players.NoData -> uiState.isLoading
                is ExploreUiState.Venues.NoData -> uiState.isLoading
                is ExploreUiState.Coaches.NoData -> uiState.isLoading
                is ExploreUiState.Countries.NoData -> uiState.isLoading
                else -> false
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                SimpleSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.surface)
                        .border(
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.inverseSurface
                            ),
                        ),
                    label = stringResource(id = R.string.search_all),
                    query = uiState.exploreQuery,
                    onQueryChange = onExploreQueryChanged,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                ExploreFilterChips(
                    uiState.exploreFilterChip,
                    uiState.exploreFilterChips,
                    onExploreChipClick
                )
                when (uiState) {
                    is ExploreUiState.Fixtures.HasFullData -> {
                        FixturesListWithHeader(
                            fixtures = uiState.favoriteFixtures,
                            color = YellowCorn,
                            textId = R.string.favorites,
                            onFixtureClick = onFixtureClick
                        )
                        FixturesListWithHeader(
                            fixtures = uiState.liveFixtures,
                            color = RedDark,
                            textId = R.string.live_score,
                            onFixtureClick = onFixtureClick
                        )
                    }
                    is ExploreUiState.Fixtures.HasOnlyLiveFixtures -> {
                        FixturesListWithHeader(
                            fixtures = uiState.liveFixtures,
                            color = RedDark,
                            textId = R.string.live_score,
                            onFixtureClick = onFixtureClick
                        )
                    }
                    is ExploreUiState.Fixtures.HasOnlyFavoriteFixtures -> {
                        FixturesListWithHeader(
                            fixtures = uiState.favoriteFixtures,
                            color = YellowCorn,
                            textId = R.string.favorites,
                            onFixtureClick = onFixtureClick
                        )
                    }
                    is ExploreUiState.Fixtures.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_fixtures
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
                    is ExploreUiState.Teams.HasFullData -> {
                        TeamsListWithHeader(
                            teams = uiState.favoriteTeams,
                            color = YellowCorn,
                            textId = R.string.favorites,
                            onTeamClick = onTeamClick
                        )
                        TeamsListWithHeader(
                            teams = uiState.teams,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.teams,
                            onTeamClick = onTeamClick
                        )
                    }
                    is ExploreUiState.Teams.HasWithoutFavorite -> {
                        TeamsListWithHeader(
                            teams = uiState.teams,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.teams,
                            onTeamClick = onTeamClick
                        )
                    }
                    is ExploreUiState.Teams.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_teams
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
                    is ExploreUiState.Players.HasFullData -> {
                        PlayersListWithHeader(
                            players = uiState.favoritePlayers,
                            color = YellowCorn,
                            textId = R.string.favorites,
                            onPlayerClick = onPlayerClick
                        )
                        PlayersListWithHeader(
                            players = uiState.players,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.players,
                            onPlayerClick = onPlayerClick
                        )
                    }
                    is ExploreUiState.Players.HasWithoutFavorite -> {
                        PlayersListWithHeader(
                            players = uiState.players,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.players,
                            onPlayerClick = onPlayerClick
                        )
                    }
                    is ExploreUiState.Players.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_players
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
                    is ExploreUiState.Coaches.HasFullData -> {
                        CoachesListWithHeader(
                            coaches = uiState.coaches,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.coaches,
                            onCoachClick = { }
                        )
                    }
                    is ExploreUiState.Coaches.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_coaches
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
                    is ExploreUiState.Venues.HasFullData -> {
                        VenuesListWithHeader(
                            venues = uiState.venue,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.venues,
                            onVenueClick = { }
                        )
                    }
                    is ExploreUiState.Venues.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_venues
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
    ErrorSnackbar(
        uiState = uiState,
        onRefreshClick = onRefreshClick,
        onErrorClear = onErrorClear,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    AppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(vertical = 8.dp),
        title = {
            Text(
                text = stringResource(id = R.string.explore),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        })
}


@Composable
private fun ExploreFilterChips(
    exploreFilterChip: FilterChip.Explore,
    exploreFilterChips: List<FilterChip.Explore>,
    onExploreFilterClick: (FilterChip.Explore) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        mainAxisAlignment = FlowMainAxisAlignment.Center
    ) {
        exploreFilterChips.forEach {
            FilterExploreChip(
                exploreFilterChip = it,
                isSelected = it == exploreFilterChip,
                onStateChanged = onExploreFilterClick,
                icon = it.icon
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterExploreChip(
    exploreFilterChip: FilterChip.Explore,
    isSelected: Boolean,
    onStateChanged: (FilterChip.Explore) -> Unit,
    icon: ImageVector
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 2.dp),
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
                text = stringResource(id = exploreFilterChip.textId),
                fontSize = 12.sp,
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
        onClick = { onStateChanged(exploreFilterChip) }
    )
}

@Composable
fun FixturesListWithHeader(
    fixtures: List<FixtureItem>,
    color: Color,
    textId: Int,
    onFixtureClick: (FixtureItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = fixtures) {
            FixtureCard(fixtureItem = it, onFixtureClick = onFixtureClick)
        }
    }
}

@Composable
fun TeamsListWithHeader(
    teams: List<TeamCountry>,
    color: Color,
    textId: Int,
    onTeamClick: (TeamCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = teams) {
            TeamCard(teamCountry = it, onTeamClick = onTeamClick)
        }
    }
}

@Composable
fun PlayersListWithHeader(
    players: List<PlayerCountry>,
    color: Color,
    textId: Int,
    onPlayerClick: (PlayerCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = players) {
            PlayerCard(playerCountry = it, onPlayerClick = onPlayerClick)
        }
    }
}

@Composable
fun CoachesListWithHeader(
    coaches: List<CoachCountry>,
    color: Color,
    textId: Int,
    onCoachClick: (CoachCountry) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(id = textId),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = color,
            )
        }
        items(items = coaches) {
            CoachCard(coachCountry = it, onCoachClick = {})
        }
    }
}

@Composable
fun VenuesListWithHeader(
    venues: List<Venue>,
    color: Color,
    textId: Int,
    onVenueClick: (Venue) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = venues) {
            VenueCard(venue = it, onVenueClick = onVenueClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VenueCard(venue: Venue = Venue.EMPTY_VENUE, onVenueClick: (Venue) -> Unit = {}) {
    Card(
        onClick = { onVenueClick(venue) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .size(width = 140.dp, height = 160.dp)
            .padding(8.dp)
            .align(Alignment.Center),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(venue.image)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = venue.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: ExploreUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (val error = uiState.error) {
        is ExploreError.NoError -> {}
        is ExploreError.EmptyTeam -> {
            val errorMessageText = stringResource(id = R.string.empty_leagues)
            val onErrorDismissState by rememberUpdatedState(onErrorClear)
            LaunchedEffect(errorMessageText) {
                snackbarHostState.showSnackbar(message = errorMessageText)
                onErrorDismissState()
            }
        }
        is ExploreError.RemoteError -> {
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