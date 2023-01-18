package com.kuba.flashscorecompose.home.screen

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.HomeUiState
import com.kuba.flashscorecompose.home.viewmodel.HomeViewModel
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FixtureCard
import com.kuba.flashscorecompose.ui.component.FlashScoreSnackbarHost
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
        onCountryClick = { countryName, isSelected ->
            viewModel.getFixturesByCountry(countryName, isSelected)
        },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.fixture.id)) },
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
    onCountryClick: (String, Boolean) -> Unit,
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (Int) -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    context: Context
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = { TopBar(context = context) },
        scaffoldState = scaffoldState,
        snackbarHost = { FlashScoreSnackbarHost(hostState = it) }
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
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Banner()
                Spacer(modifier = Modifier.size(16.dp))
                when (uiState) {
                    is HomeUiState.HasData -> {
                        CountriesWidget(
                            modifier = Modifier,
                            uiState.countryItems,
                            onCountryClick
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        WidgetFixtures(
                            modifier,
                            uiState.leagueWithFixtures,
                            onFixtureClick,
                            onLeagueClick
                        )
                    }
                    is HomeUiState.NoData -> EmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        iconId = R.drawable.ic_close,
                        contentDescriptionId = R.string.load_data_from_network,
                        textId = R.string.no_fixtures_and_countries,
                        onRefreshClick = onRefreshClick
                    )
                }
            }
        }
    }
    ErrorSnackbar(uiState, onRefreshClick, onErrorClear, scaffoldState)
}

@Composable
fun WidgetFixtures(
    modifier: Modifier,
    leagueWithFixtures: Map<League, List<FixtureItem>>,
    onFixtureClick: (FixtureItem) -> Unit,
    onLeagueClick: (Int) -> Unit
) {
    Column(modifier = modifier) {
        leagueWithFixtures.forEach {
            Spacer(modifier = Modifier.size(24.dp))
            HeaderLeague(it.key, onLeagueClick)
            Spacer(modifier = Modifier.size(16.dp))
            it.value.forEach { fixtureItem ->
                FixtureCard(fixtureItem, onFixtureClick)
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
fun TopBar(context: Context) {
    AppTopBar(
        title = { Text(text = stringResource(id = R.string.live_score)) },
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
                    contentDescription = stringResource(id = R.string.search)
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
                    contentDescription = stringResource(id = R.string.notifications)
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
                    colors = listOf(Blue500, Blue800)
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
                        color = Color.White,
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
fun CountriesWidget(
    modifier: Modifier,
    countries: List<Country>,
    onCountryClick: (String, Boolean) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    val selectedCountryName = remember { mutableStateOf("") }
    LazyRow(modifier = modifier, state = state) {
        items(countries) { country ->
            CountryWidgetCard(
                country = country,
                isSelected = selectedCountryName.value == country.name
            ) {
                val isSelected = selectedCountryName.value == it
                selectedCountryName.value = if (isSelected) "" else it
                onCountryClick(it, isSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryWidgetCard(
    country: Country,
    isSelected: Boolean,
    onCountryClick: (String) -> Unit
) {
    Card(
        onClick = { onCountryClick(country.name) },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.size(width = 115.dp, height = 130.dp),
    ) {
        val isSelectedModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp),
                brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
            )
        val normalModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp), color = Dark500
            )
        Column(
            modifier = if (isSelected) isSelectedModifier else normalModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(country.flag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
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
fun HeaderLeague(league: League, onLeagueClick: (Int) -> Unit) {
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
                    .decoderFactory(SvgDecoder.Factory())
                    .data(league.countryFlag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
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
                    color = Color.White
                )
                Text(
                    text = league.countryName,
                    fontSize = 12.sp,
                    color = Grey
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

@Composable
private fun ErrorSnackbar(
    uiState: HomeUiState,
    onRefreshClick: () -> Unit,
    onErrorClear: () -> Unit,
    scaffoldState: ScaffoldState
) {
    when (val error = uiState.error) {
        is HomeError.NoError -> {}
        is HomeError.RemoteError -> {
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