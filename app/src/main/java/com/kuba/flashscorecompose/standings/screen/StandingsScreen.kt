package com.kuba.flashscorecompose.standings.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.standings.model.GoalsStanding
import com.kuba.flashscorecompose.data.standings.model.InformationStanding
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.standings.model.StandingsUiState
import com.kuba.flashscorecompose.standings.viewmodel.StandingsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import com.kuba.flashscorecompose.data.standings.model.Standings
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
        navigator = navigator,
        onRefreshClick = { viewModel.refreshStandings() },
        onCountryClick = { countryName, isSelected ->
            viewModel.getStandingsByCountry(countryName, isSelected)
        },
        onStandingsClick = {},
        onErrorClear = {},
        scaffoldState = scaffoldState,
        onStandingsQueryChanged = {}
    )
}

@Composable
fun StandingsScreen(
    modifier: Modifier = Modifier,
    uiState: StandingsUiState,
    navigator: DestinationsNavigator,
    onRefreshClick: () -> Unit,
    onCountryClick: (String, Boolean) -> Unit,
    onStandingsClick: (Standings) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState,
    onStandingsQueryChanged: (String) -> Unit
) {
    val scrollState = rememberScrollState()
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
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                SimpleSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(width = 2.dp, color = GreyDark),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    label = stringResource(id = R.string.search_standings),
                    query = uiState.standingsQuery,
                    onQueryChange = onStandingsQueryChanged,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "",
                            tint = GreyTextDark
                        )
                    }
                )
                when (uiState) {
                    is StandingsUiState.HasData -> {
                        Spacer(modifier = Modifier.size(28.dp))
                        CountriesWidget(
                            countries = uiState.countryItems,
                            onCountryClick = onCountryClick
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        StandingsWidget(uiState.standings, onStandingsClick)
                    }
                    is StandingsUiState.NoData -> {
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

@Composable
fun StandingsWidget(
    standings: List<Standings>,
    onStandingsClick: (Standings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .height(500.dp)
    ) {
        items(
            items = standings,
            key = { it.leagueId }
        ) {
            StandingWithLeague(it, onStandingsClick)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun StandingWithLeague(
    it: Standings,
    onStandingsClick: (Standings) -> Unit
) {
    Column {
        HeaderLeague(league = it.league, onLeagueClick = {})
        Spacer(modifier = Modifier.size(16.dp))
        StandingCard(it.standings)
    }
}

@Composable
fun StandingCard(standings: List<StandingItem>) {
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

@Composable
private fun TopBar() {
    AppTopBar(title = { Text(text = stringResource(id = R.string.standings), color = Color.White) })
}

@Composable
private fun StandingHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
                .size(24.dp),
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
            modifier = Modifier.weight(5f),
            text = standingItem.team.name,
            fontSize = 12.sp,
            color = White
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


@Preview
@Composable
fun Test() {
    val standings = listOf(
        StandingItem(
            InformationStanding(0, GoalsStanding.EMPTY_GOALS_STANDING, 0, 0, 0),
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
            .border(
                shape = RoundedCornerShape(16.dp),
                border =BorderStroke(width = 2.dp, color = GreyDark),
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