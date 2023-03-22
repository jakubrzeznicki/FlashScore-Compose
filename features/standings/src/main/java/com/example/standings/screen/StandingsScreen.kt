package com.example.standings.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.model.country.Country
import com.example.model.league.League
import com.example.model.standings.Standing
import com.example.model.standings.StandingItem
import com.example.standings.R
import com.example.standings.model.StandingsUiState
import com.example.standings.navigation.StandingsNavigator
import com.example.standings.viewmodel.StandingsViewModel
import com.example.ui.composables.*
import com.example.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 23/12/2022.
 */

private const val SETUP_STANDINGS_KEY = "SETUP_STANDINGS_KEY"
private const val TEAMS = 4

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun StandingsRoute(
    navigator: StandingsNavigator,
    viewModel: StandingsViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val fixturesScrollState = rememberLazyListState()
    val countryScrollState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )
    LaunchedEffect(key1 = SETUP_STANDINGS_KEY) { viewModel.setup() }
    StandingsScreen(
        uiState = uiState,
        fixturesScrollState = fixturesScrollState,
        countryScrollState = countryScrollState,
        pullRefreshState = pullRefreshState,
        onCountryClick = { country, isSelected ->
            viewModel.updateSelectedCountry(country, isSelected)
        },
        onStandingsClick = { navigator.openStandingsDetails(it.league) },
        onLeagueClick = { navigator.openLeagueDetails(it) },
        onStandingsQueryChanged = { viewModel.updateStandingsQuery(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StandingsScreen(
    modifier: Modifier = Modifier,
    fixturesScrollState: LazyListState,
    countryScrollState: LazyListState,
    uiState: StandingsUiState,
    pullRefreshState: PullRefreshState,
    onCountryClick: (Country, Boolean) -> Unit,
    onStandingsClick: (Standing) -> Unit,
    onLeagueClick: (League) -> Unit,
    onStandingsQueryChanged: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            pullRefreshState = pullRefreshState,
            empty = when (uiState) {
                is StandingsUiState.HasAllData -> false
                is StandingsUiState.HasOnlyCountries -> false
                is StandingsUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading
        ) {
            val scrollState = rememberScrollState()
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
                            )
                        ),
                    label = stringResource(id = R.string.search_standings),
                    query = uiState.standingsQuery,
                    onQueryChange = onStandingsQueryChanged,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))
                when (uiState) {
                    is StandingsUiState.HasAllData -> {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            state = fixturesScrollState
                        ) {
                            item {
                                LazyRow(modifier = modifier, state = countryScrollState) {
                                    items(uiState.countries) { country ->
                                        CountryWidgetCard(
                                            country = country,
                                            isSelected = uiState.selectedCountry == country,
                                            onCountryClick = onCountryClick
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                            items(items = uiState.standings) {
                                StandingWithLeagueItem(it, onStandingsClick, onLeagueClick)
                            }
                        }
                    }
                    is StandingsUiState.HasOnlyCountries -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            LazyRow(modifier = modifier, state = countryScrollState) {
                                items(uiState.countries) { country ->
                                    CountryWidgetCard(
                                        country = country,
                                        isSelected = uiState.selectedCountry == country,
                                        onCountryClick = onCountryClick
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(24.dp))
                            EmptyState(
                                modifier = Modifier.fillMaxWidth(),
                                textId = R.string.no_standings
                            )
                        }
                    }
                    is StandingsUiState.NoData -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
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
                                    painter = painterResource(id = com.example.ui.R.drawable.ic_close),
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
}

@Composable
fun StandingWithLeagueItem(
    standing: Standing,
    onStandingsClick: (Standing) -> Unit,
    onLeagueClick: (League) -> Unit
) {
    Column(
        Modifier
            .clickable { onStandingsClick(standing) }
            .padding(bottom = 16.dp)
    ) {
        LeagueHeader(
            league = standing.league,
            onLeagueClick = { onLeagueClick(standing.league) }
        )
        StandingCard(standing.standingItems)
    }
}

@Composable
fun StandingCard(standingItems: List<StandingItem>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            StandingHeaderRow()
            Divider(
                color = MaterialTheme.colorScheme.inverseSurface,
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.End)
            )
            standingItems.take(TEAMS).forEach { standingItem ->
                StandingElementRow(standingItem)
                Divider(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.End)
                )
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
            .padding(top = 8.dp),
        title = {
            Text(
                text = stringResource(id = com.example.ui.R.string.standings),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        }
    )
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
            text = stringResource(id = com.example.ui.R.string.team),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.played_short),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = com.example.ui.R.string.wins),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = com.example.ui.R.string.loses),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = com.example.ui.R.string.draws),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = com.example.ui.R.string.goals_diff),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = com.example.ui.R.string.points),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
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
            placeholder = painterResource(id = com.example.ui.R.drawable.ic_close),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier
                .weight(5f)
                .padding(end = 4.dp),
            text = standingItem.team.name,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.played.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.win.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.lose.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.all.draw.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.goalsDiff.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = standingItem.points.toString(),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
