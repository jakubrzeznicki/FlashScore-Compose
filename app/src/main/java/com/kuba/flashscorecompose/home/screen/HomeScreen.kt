package com.kuba.flashscorecompose.home.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.ColumnScopeInstanceImpl.weight
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.CountryListScreenDestination
import com.kuba.flashscorecompose.destinations.LeaguesListScreenDestination
import com.kuba.flashscorecompose.destinations.MatchDetailsContainerScreenDestination
import com.kuba.flashscorecompose.home.model.HomeUiState
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.theme.*
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
    viewModel: HomeViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_HOME_KEY) { viewModel.setup() }
    HomeScreen(
        uiState = uiState,
        navigator = navigator,
        onRefreshClick = { viewModel.refresh() },
        onCountryClick = { navigator.navigate(LeaguesListScreenDestination(it)) },
        onFixtureClick = { navigator.navigate(MatchDetailsContainerScreenDestination(it.fixture.id)) },
        onLeagueClick = { },
        onErrorClear = { viewModel.cleanError() },
        scaffoldState = scaffoldState,
        context = context
    )
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    navigator: DestinationsNavigator,
    onRefreshClick: () -> Unit,
    onCountryClick: (String) -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (Int) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    context: Context
) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = { TopBar(context = context) }, scaffoldState = scaffoldState) {
        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.HasData -> false
                is HomeUiState.NoData -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshClick
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(PaddingValues(16.dp)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Banner()
                Spacer(modifier = Modifier.size(16.dp))
                when (uiState) {
                    is HomeUiState.HasData -> CountryWidgetsList(
                        modifier = Modifier,
                        uiState.countryItems,
                        onCountryClick
                    )
                    else -> EmptyCountryWidget()
                }
                Spacer(modifier = Modifier.size(16.dp))
                when (uiState) {
                    is HomeUiState.HasData -> WidgetFixtures(
                        modifier,
                        uiState.fixtureItems,
                        onFixtureClick,
                        onLeagueClick
                    )
                    else -> EmptyFixtureWidget()
                }
            }
        }
    }
}

@Composable
fun WidgetFixtures(
    modifier: Modifier,
    fixtureItems: List<FixtureItem>,
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (Int) -> Unit
) {
    val leagueWithFixtures = fixtureItems.groupBy { it.league }
    leagueWithFixtures.forEach {
        Log.d("TEST_LOG", "leaguieFixtuires league - ${it.key}")
        it.value.forEach {
            Log.d("TEST_LOG", "leaguieFixtuires fixurte- ${it}")
        }
    }
    Column(modifier = modifier) {
        leagueWithFixtures.forEach {
            Spacer(modifier = Modifier.size(24.dp))
            HeaderWidgetLeagueCard(it.key, onLeagueClick)
            Spacer(modifier = Modifier.size(16.dp))
            it.value.forEach { fixtureItem ->
                FixtureWidgetCard(fixtureItem, onFixtureClick)
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
fun TopBar(context: Context) {
    AppTopBar(title = {
        Text(text = stringResource(id = R.string.live_score))
    }, actions = {
        IconButton(modifier = Modifier
            .padding(horizontal = 12.dp)
            .size(24.dp),
            onClick = { Toast.makeText(context, "Search", Toast.LENGTH_SHORT) }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
        }
        IconButton(modifier = Modifier
            .padding(horizontal = 12.dp)
            .size(24.dp),
            onClick = { Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT) }) {
            Icon(
                imageVector = Icons.Filled.Notifications, contentDescription = "notifications"
            )
        }
    })
}


@Composable
fun Banner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Blue500, Blue800),
                ), shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            Modifier
                .padding(PaddingValues(start = 18.dp, top = 24.dp, bottom = 24.dp))
                .fillMaxWidth(0.5F)
        ) {
            Row(
                Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(46.dp),
                    )
                    .padding(PaddingValues(8.dp)),
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
                    color = Black500,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.text_banner),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 25.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.text_second_banner), fontSize = 12.sp
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
fun CountryWidgetsList(
    modifier: Modifier,
    countries: List<Country>,
    onCountryClick: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    val selected = remember { mutableStateOf(0) }
    LazyRow(modifier = modifier, state = state) {
        items(countries) {
            CountryWidgetCard(
                country = it,
                selectedIndex = selected,
                onCountryClick = onCountryClick
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryWidgetCard(
    country: Country,
    selectedIndex: MutableState<Int>,
    onCountryClick: (String) -> Unit
) {
    Card(
        onClick = { onCountryClick(country.code) },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.size(width = 115.dp, height = 130.dp),
    ) {
        val isActiveModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp), brush = Brush.horizontalGradient(
                    colors = listOf(
                        LightOrange, Orange
                    )
                )
            )
        val normalModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp), color = Dark500
            )
        Column(
            //modifier = if (leagueWidgetItem.isActive) isActiveModifier else normalModifier,
            modifier = normalModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory()).data(country.flag)
                    .size(Size.ORIGINAL).crossfade(true).build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = country.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun HeaderWidgetLeagueCard(league: League, onLeagueClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onLeagueClick(league.id) }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(24.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory()).data(league.logo)
                    .size(Size.ORIGINAL).crossfade(true).build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    text = league.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White,
                )
                Text(
                    text = league.countryName,
                    fontSize = 12.sp,
                    color = Grey,
                )
            }
        }
        IconButton(modifier = Modifier
            .padding(horizontal = 12.dp)
            .size(16.dp), onClick = { }) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "")
        }
    }
}

data class Team(val name: String, val logo: String)
data class FixtureData(
    val homeTeam: Team, val awayTeam: Team, val homeTeamScore: Int, val awayTeamScore: Int
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FixtureWidgetCard(fixtureItem: FixtureItem, onFixtureClick: (FixtureItem) -> Unit) {
    Card(
        onClick = { onFixtureClick(fixtureItem) },
        backgroundColor = GreyLight,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(30.dp), color = GreyDark
                        )
                        .size(36.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = PaddingValues(4.dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(SvgDecoder.Factory())
                            .data(fixtureItem.homeTeam.logo).size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(30.dp), color = GreyDark
                        )
                        .size(36.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = PaddingValues(4.dp)),
                        model = ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(SvgDecoder.Factory())
                            .data(fixtureItem.awayTeam.logo).size(Size.ORIGINAL)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Row(
                Modifier
                    .weight(7f)
                    .padding(end = 4.dp)) {
                Column(
                    Modifier.weight(6f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fixtureItem.homeTeam.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1

                    )
                    Text(
                        text = fixtureItem.goals.home.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                    )
                }
                Column(
                    Modifier
                        .weight(2f)
                        .padding(horizontal = 1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "vs",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = "-",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
                Column(
                    Modifier.weight(6f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fixtureItem.awayTeam.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = fixtureItem.goals.away.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                        color = GreyDark
                    )
                    .size(70.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fixtureItem.fixture.status.short,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun EmptyCountryWidget() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier.size(40.dp),
            contentDescription = ""
        )
        Text("No Countries", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun EmptyFixtureWidget() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier.size(40.dp),
            contentDescription = ""
        )
        Text("No Fixture", modifier = Modifier.padding(16.dp))
    }
}