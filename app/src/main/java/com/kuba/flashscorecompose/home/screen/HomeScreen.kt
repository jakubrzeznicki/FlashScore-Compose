package com.kuba.flashscorecompose.home.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.destinations.LeagueDetailsRouteDestination
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.HomeUiState
import com.kuba.flashscorecompose.home.model.LeagueFixturesData
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.theme.FlashScoreTypography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

/**
 * Created by jrzeznicki on 21/12/2022.
 */

private const val SETUP_HOME_KEY = "SETUP_HOME_KEY"

@Destination(route = "home", start = true)
@Composable
fun HomeScreenRoute(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = SETUP_HOME_KEY) { viewModel.setup() }
    HomeScreen(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onCountryClick = { country, isSelected ->
            viewModel.updateSelectedCountry(country, isSelected)
        },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.fixture.id)) },
        onLeagueClick = { league, date ->
            navigator.navigate(LeagueDetailsRouteDestination(league.id, league.season))
        },
        onErrorClear = { viewModel.cleanError() },
        snackbarHostState = snackbarHostState,
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onRefreshClick: () -> Unit,
    onCountryClick: (Country, Boolean) -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (League, String) -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = { TopBar(context = context) },
        snackbarHost = { FlashScoreSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        LoadingContent(
            modifier = modifier.padding(paddingValues),
            empty = when (uiState) {
                is HomeUiState.HasData -> false
                is HomeUiState.NoData -> uiState.isLoading
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
                when (uiState) {
                    is HomeUiState.HasData -> {
                        item {
                            Banner()
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                        item {
                            CountriesWidget(
                                countries = uiState.countries,
                                selectedItem = uiState.selectedCountry,
                                onCountryClick = onCountryClick
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                        items(items = uiState.leagueFixturesDataList) {
                            FixturesWidget(
                                leagueFixturesData = it,
                                onFixtureClick = onFixtureClick,
                                onLeagueClick = onLeagueClick,
                                uiState.date
                            )
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    }
                    is HomeUiState.NoData -> {
                        item {
                            EmptyState(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                iconId = R.drawable.ic_close,
                                contentDescriptionId = R.string.load_data_from_network,
                                textId = R.string.no_fixtures_and_countries
                            )
                        }
                    }
                }
            }
        }
    }
    ErrorSnackbar(uiState, onRefreshClick, onErrorClear, snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(context: Context) {
    AppTopBar(
        title = {
            Text(
                text = stringResource(id = R.string.live_score),
                color = MaterialTheme.colorScheme.onSecondary,
                style = FlashScoreTypography.headlineSmall
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = {
                    Toast.makeText(context, R.string.search, Toast.LENGTH_SHORT).show()
                }) {
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
                onClick = {
                    Toast.makeText(context, R.string.notifications, Toast.LENGTH_SHORT).show()
                }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = stringResource(id = R.string.notifications),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        })
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
                    fontSize = 12.sp,
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
                color = MaterialTheme.colorScheme.onSecondary,
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
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (League, String) -> Unit,
    date: String
) {
    LeagueHeader(leagueFixturesData.league, date, onLeagueClick)
    leagueFixturesData.fixtures.forEach { fixtureItem ->
        FixtureCard(fixtureItem, onFixtureClick)
    }
}

@Composable
private fun ErrorSnackbar(
    uiState: HomeUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    when (val error = uiState.error) {
        is HomeError.NoError -> {}
        is HomeError.RemoteError -> {
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