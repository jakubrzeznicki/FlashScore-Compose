package com.kuba.flashscorecompose.fixturedetails.container.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.pager.*
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Goals
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Status
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsUiState
import com.kuba.flashscorecompose.fixturedetails.container.viewmodel.FixtureDetailsViewModel
import com.kuba.flashscorecompose.fixturedetails.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val SETUP_FIXTURE_DETAILS_CONTAINER_KEY = "SETUP_FIXTURE_DETAILS_CONTAINER_KEY"

@Destination(route = "home/fixturedetails")
@Composable
fun FixtureDetailsRoute(
    fixtureId: Int,
    viewModel: FixtureDetailsViewModel = getViewModel { parametersOf(fixtureId) },
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_FIXTURE_DETAILS_CONTAINER_KEY) { viewModel.setup() }
    FixtureDetailsScreen(
        uiState = uiState,
        navigator = navigator,
        onTeamClick = {},
        scaffoldState = scaffoldState
    )
}

@Composable
fun FixtureDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: FixtureDetailsUiState,
    navigator: DestinationsNavigator,
    onTeamClick: (Team) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopBar(navigator, uiState) },
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is FixtureDetailsUiState.HasData -> {
                    HeaderMatchInfo(uiState.fixtureItem, onTeamClick)
                    Spacer(modifier = Modifier.size(32.dp))
                    FixtureDetailsTabs(uiState, navigator)
                }
                is FixtureDetailsUiState.NoData -> EmptyState(
                    modifier = Modifier.fillMaxWidth(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.empty_database,
                    textId = R.string.no_fixture_details,
                    onRefreshClick = {}
                )
            }
        }
    }
}

@Composable
fun TopBar(navigator: DestinationsNavigator, uiState: FixtureDetailsUiState) {
    val titleText = when (uiState) {
        is FixtureDetailsUiState.HasData -> uiState.fixtureItem.league.round
        else -> ""
    }
    AppTopBar(
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        title = { Text(text = titleText) }
    )
}

@Composable
fun HeaderMatchInfo(fixtureItem: FixtureItem, onTeamClick: (Team) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(team = fixtureItem.homeTeam, Modifier.weight(2f), onTeamClick)
        TeamInfoScore(fixtureItem.goals, fixtureItem.fixture.status, Modifier.weight(4f))
        TeamInfo(team = fixtureItem.awayTeam, Modifier.weight(2f), onTeamClick)
    }
}

@Composable
fun TeamInfo(team: Team, modifier: Modifier, onTeamClick: (Team) -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(50.dp), color = GreyDark)
                .border(width = 2.dp, shape = RoundedCornerShape(50.dp), color = GreyLight)
                .size(70.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = PaddingValues(12.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(team.logo)
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
            text = team.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TeamInfoScore(goals: Goals, status: Status, modifier: Modifier) {
    Column(
        modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${goals.home} - ${goals.away}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = status.long,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = "${status.elapsed}'",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FixtureDetailsTabs(uiState: FixtureDetailsUiState.HasData, navigator: DestinationsNavigator) {
    Column {
        val tabs = listOf(
            TabItem.MatchDetail(
                uiState.fixtureItem.id,
                uiState.fixtureItem.league.id,
                uiState.fixtureItem.league.round,
                navigator
            ),
            TabItem.LineUp(uiState.fixtureItem.id, navigator),
            TabItem.HeadToHead(
                uiState.fixtureItem.homeTeam,
                uiState.fixtureItem.awayTeam,
                uiState.fixtureItem.season,
                uiState.fixtureItem.id,
                navigator
            )
        )
        val pagerState = rememberPagerState()
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.padding(16.dp)) {
            FixtureDetailsTabs(tabs = tabs, pagerState = pagerState, coroutineScope)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FixtureDetailsTabs(
    tabs: List<TabItem>,
    pagerState: PagerState,
    coroutineScope: CoroutineScope
) {
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = Color.White,
        indicator = {
            Box {}
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tab ->
            FixtureDetailsTab(
                pagerState = pagerState,
                coroutineScope = coroutineScope,
                tab = tab,
                index = index
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun FixtureDetailsTab(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    tab: TabItem,
    index: Int
) {
    Tab(
        modifier = if (pagerState.currentPage == index) {
            Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(LightOrange, Orange))
                )
        } else {
            Modifier
                .clip(RoundedCornerShape(50))
        },
        text = { Text(tab.title) },
        selected = pagerState.currentPage == index,
        onClick = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}