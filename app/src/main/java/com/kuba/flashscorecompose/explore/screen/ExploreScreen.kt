package com.kuba.flashscorecompose.explore.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.*
import com.kuba.flashscorecompose.explore.model.ExploreUiState
import com.kuba.flashscorecompose.explore.model.TeamWrapper
import com.kuba.flashscorecompose.explore.viewmodel.ExploreViewModel
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = SETUP_EXPLORE_KEY) { viewModel.setup() }
    ExploreScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.fixtureItem)) },
        onFixtureFavoriteClick = { viewModel.addFixtureToFavorite(it) },
        onTeamClick = {
            navigator.navigate(
                TeamDetailsRouteDestination(
                    it.team,
                    it.team.leagueId,
                    it.team.season
                )
            )
        },
        onTeamFavoriteClick = { viewModel.addTeamToFavorite(it) },
        onPlayerClick = {
            navigator.navigate(
                PlayerDetailsRouteDestination(
                    it.player.id,
                    it.player.team,
                    it.player.season
                )
            )
        },
        onPlayerFavoriteClick = { viewModel.addPlayerToFavorite(it) },
        onLeagueClick = { navigator.navigate(LeagueDetailsRouteDestination(it)) },
        onExploreQueryChanged = { viewModel.updateExploreQuery(it) },
        onExploreChipClick = { viewModel.changeExploreView(it as FilterChip.Explore) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    uiState: ExploreUiState,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFixtureFavoriteClick: (FixtureItemWrapper) -> Unit,
    onTeamClick: (TeamWrapper) -> Unit,
    onTeamFavoriteClick: (TeamWrapper) -> Unit,
    onPlayerClick: (PlayerWrapper) -> Unit,
    onPlayerFavoriteClick: (PlayerWrapper) -> Unit,
    onLeagueClick: (League) -> Unit,
    onExploreQueryChanged: (String) -> Unit,
    onExploreChipClick: (FilterChip) -> Unit
) {
    Scaffold(topBar = { TopBar() }) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            empty = when (uiState) {
                is ExploreUiState.Fixtures.NoData -> uiState.isLoading
                is ExploreUiState.Teams.NoData -> uiState.isLoading
                is ExploreUiState.Players.NoData -> uiState.isLoading
                is ExploreUiState.Venues.NoData -> uiState.isLoading
                is ExploreUiState.Coaches.NoData -> uiState.isLoading
                is ExploreUiState.Leagues.NoData -> uiState.isLoading
                else -> false
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            val scrollEmptyStateState = rememberScrollState()
            val fixturesLazyListState = rememberLazyListState()
            val teamsLazyListState = rememberLazyListState()
            val playersLazyListState = rememberLazyListState()
            val coachesLazyListState = rememberLazyListState()
            val venuesLazyGridState = rememberLazyGridState()
            val leaguesLazyListState = rememberLazyListState()
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
                        FixturesDoubleListWithHeader(
                            fixtures = uiState.liveFixtures,
                            favoriteFixtures = uiState.favoriteFixtures,
                            state = fixturesLazyListState,
                            color = MaterialTheme.colorScheme.error,
                            favoriteColor = MaterialTheme.colorScheme.primary,
                            textId = R.string.live_score,
                            favoriteTextId = R.string.favorites,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFixtureFavoriteClick
                        )
                    }
                    is ExploreUiState.Fixtures.HasOnlyLiveFixtures -> {
                        FixturesListWithHeader(
                            fixtures = uiState.liveFixtures,
                            state = fixturesLazyListState,
                            color = MaterialTheme.colorScheme.error,
                            textId = R.string.live_score,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFixtureFavoriteClick
                        )
                    }
                    is ExploreUiState.Fixtures.HasOnlyFavoriteFixtures -> {
                        FixturesListWithHeader(
                            fixtures = uiState.favoriteFixtures,
                            state = fixturesLazyListState,
                            color = MaterialTheme.colorScheme.primary,
                            textId = R.string.favorites,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFixtureFavoriteClick
                        )
                    }
                    is ExploreUiState.Fixtures.NoData -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollEmptyStateState)
                        ) {
                            EmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                textId = R.string.no_fixtures_live
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
                    is ExploreUiState.Teams.HasFullData -> {
                        TeamsDoubleListWithHeader(
                            teamWrappers = uiState.teams,
                            favoriteTeamWrappers = uiState.favoriteTeams,
                            state = teamsLazyListState,
                            color = MaterialTheme.colorScheme.onSecondary,
                            favoriteColor = MaterialTheme.colorScheme.primary,
                            textId = R.string.teams,
                            favoriteTextId = R.string.favorites,
                            onTeamClick = onTeamClick,
                            onTeamFavoriteClick = onTeamFavoriteClick
                        )
                    }
                    is ExploreUiState.Teams.HasWithoutFavorite -> {
                        TeamsListWithHeader(
                            teams = uiState.teams,
                            state = teamsLazyListState,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.teams,
                            onTeamClick = onTeamClick,
                            onTeamFavoriteClick = onTeamFavoriteClick
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
                        PlayersDoubleListWithHeader(
                            players = uiState.players,
                            favoritePlayers = uiState.favoritePlayers,
                            state = playersLazyListState,
                            color = MaterialTheme.colorScheme.onSecondary,
                            favoriteColor = MaterialTheme.colorScheme.primary,
                            textId = R.string.players,
                            favoriteTextId = R.string.favorites,
                            onPlayerClick = onPlayerClick,
                            onPlayerFavoriteClick = onPlayerFavoriteClick
                        )
                    }
                    is ExploreUiState.Players.HasWithoutFavorite -> {
                        PlayersListWithHeader(
                            players = uiState.players,
                            color = MaterialTheme.colorScheme.onSecondary,
                            state = playersLazyListState,
                            textId = R.string.players,
                            onPlayerClick = onPlayerClick,
                            onPlayerFavoriteClick = onPlayerFavoriteClick
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
                            state = coachesLazyListState,
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
                        VenuesList(
                            venues = uiState.venue,
                            state = venuesLazyGridState,
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
                    is ExploreUiState.Leagues.HasFullData -> {
                        LeagueListWithHeader(
                            leagues = uiState.leagues,
                            state = leaguesLazyListState,
                            color = MaterialTheme.colorScheme.onSecondary,
                            textId = R.string.leagues,
                            onLeagueClick = onLeagueClick
                        )
                    }
                    is ExploreUiState.Leagues.NoData -> {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_leagues
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