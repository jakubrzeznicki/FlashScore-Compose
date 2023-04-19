package com.example.home.screen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.home.R
import com.example.home.model.HomeUiState
import com.example.home.model.LeagueFixturesData
import com.example.home.navigation.HomeNavigator
import com.example.home.viewmodel.HomeViewModel
import com.example.model.country.Country
import com.example.model.fixture.FixtureItemWrapper
import com.example.model.league.League
import com.example.ui.composables.*
import com.example.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.getViewModel
import com.example.ui.R as uiR

/**
 * Created by jrzeznicki on 21/12/2022.
 */

private const val SETUP_HOME_KEY = "SETUP_HOME_KEY"

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun Home(
    navigator: HomeNavigator,
    viewModel: HomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val fixturesScrollState = rememberLazyListState()
    val countryScrollState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )
    LaunchedEffect(key1 = SETUP_HOME_KEY) { viewModel.setup() }
    HomeScreen(
        uiState = uiState,
        onCountryClick = { country, isSelected ->
            viewModel.updateSelectedCountry(country, isSelected)
        },
        onFixtureClick = { navigator.openFixtureDetails(it.fixtureItem.id) },
        onFavoriteFixtureClick = { viewModel.addFixtureToFavorite(it) },
        onLeagueClick = { navigator.openLeagueDetails(it) },
        onSearchClick = { viewModel.onSearchClick() },
        onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
        onNavigationsClick = { navigator.openNotifications() },
        fixturesScrollState = fixturesScrollState,
        countryScrollState = countryScrollState,
        pullRefreshState = pullRefreshState,
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onCountryClick: (Country, Boolean) -> Unit,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFavoriteFixtureClick: (FixtureItemWrapper) -> Unit,
    onLeagueClick: (League) -> Unit,
    onSearchClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onNavigationsClick: () -> Unit,
    fixturesScrollState: LazyListState,
    countryScrollState: LazyListState,
    pullRefreshState: PullRefreshState,
    context: Context
) {
    Scaffold(
        topBar = {
            TopBar(
                onSearchClick = onSearchClick,
                onNavigationsClick = onNavigationsClick
            )
        }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            pullRefreshState = pullRefreshState,
            empty = when (uiState) {
                is HomeUiState.HasAllData -> false
                is HomeUiState.HasOnlyCountries -> false
                is HomeUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 64.dp),
                state = fixturesScrollState
            ) {
                item {
                    SearchEditText(
                        uiState.isSearchExpanded,
                        uiState.searchQuery,
                        onSearchQueryChanged
                    )
                    Banner()
                    Spacer(modifier = Modifier.size(16.dp))
                }
                when (uiState) {
                    is HomeUiState.HasAllData -> {
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
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                        items(items = uiState.leagueFixturesDataList) {
                            FixturesWidget(
                                leagueFixturesData = it,
                                context = context,
                                onFixtureClick = onFixtureClick,
                                onLeagueClick = onLeagueClick,
                                onFavoriteClick = onFavoriteFixtureClick
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    }
                    is HomeUiState.HasOnlyCountries -> {
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
                            Spacer(modifier = Modifier.size(24.dp))
                            EmptyState(
                                modifier = Modifier.fillMaxWidth(),
                                textId = R.string.no_fixtures_home
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(8.dp),
                                    painter = painterResource(id = uiR.drawable.ic_close),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            }
                        }
                    }
                    is HomeUiState.NoData -> {
                        item {
                            EmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                textId = R.string.no_fixtures_and_countries
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(8.dp),
                                    painter = painterResource(id = uiR.drawable.ic_close),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onSearchClick: () -> Unit,
    onNavigationsClick: () -> Unit
) {
    AppTopBar(
        modifier = Modifier
            .height(48.dp)
            .padding(top = 8.dp),
        title = {
            Text(
                text = stringResource(id = uiR.string.live_score),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { onSearchClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { onNavigationsClick() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = stringResource(id = uiR.string.notifications),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )
}

@Composable
private fun SearchEditText(
    isSearchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    AnimatedVisibility(visible = isSearchExpanded) {
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
            label = stringResource(id = R.string.search_home),
            query = searchQuery,
            onQueryChange = onSearchQueryChanged,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        )
    }
    if (isSearchExpanded) Spacer(modifier = Modifier.size(16.dp))
}

@Composable
private fun Banner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.tertiaryContainer
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            Modifier
                .padding(start = 18.dp, top = 24.dp, bottom = 24.dp)
                .fillMaxWidth(0.5F)
        ) {
            Row(
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = RoundedCornerShape(46.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.football_icon),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(id = R.string.football),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.text_banner),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 25.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.text_second_banner),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Image(
            painter = painterResource(id = R.drawable.salah_liverpool),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun FixturesWidget(
    leagueFixturesData: LeagueFixturesData,
    context: Context,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onLeagueClick: (League) -> Unit,
    onFavoriteClick: (FixtureItemWrapper) -> Unit
) {
    LeagueHeader(leagueFixturesData.league, onLeagueClick)
    leagueFixturesData.fixtureWrappers.forEach { fixtureWrapper ->
        FixtureCard(fixtureWrapper, context, onFixtureClick, onFavoriteClick)
    }
}
