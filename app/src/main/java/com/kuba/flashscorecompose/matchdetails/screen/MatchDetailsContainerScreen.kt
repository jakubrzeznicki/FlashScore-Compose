package com.kuba.flashscorecompose.matchdetails.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.kuba.flashscorecompose.home.FixtureData
import com.kuba.flashscorecompose.home.Team
import com.kuba.flashscorecompose.matchdetails.tabs.TabItem
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 23/12/2022.
 */
@Destination(route = "home/matchdetailscontainer")
@Composable
fun MatchDetailsContainerScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(topBar = { TopBar(context = context, navigator) }, scaffoldState = scaffoldState) {
        Column(
            Modifier
                .fillMaxWidth()
                // .verticalScroll(scrollState)
                .padding(PaddingValues(16.dp)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            HeaderMatchInfo(
                fixtureData = FixtureData(
                    homeTeam = Team(
                        name = "Burnley",
                        logo = "https://media.api-sports.io/football/teams/44.png"
                    ),
                    awayTeam = Team(
                        name = "Manchester United",
                        logo = "https://media.api-sports.io/football/teams/33.png"
                    ),
                    homeTeamScore = 0,
                    awayTeamScore = 1
                )
            )
            Spacer(modifier = Modifier.size(32.dp))
            MatchDetailsTabs()
        }
    }
}

@Composable
fun TopBar(context: Context, navigator: DestinationsNavigator) {
    AppTopBar(
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { navigator.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }
        },
        title = {
            Text(text = "UEFA Champions League")
        }
    )
}

@Composable
fun HeaderMatchInfo(fixtureData: FixtureData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TeamInfo(team = fixtureData.homeTeam)
        TeamInfoScore(fixtureData = fixtureData)
        TeamInfo(team = fixtureData.awayTeam)
    }
}

@Composable
fun TeamInfo(team: Team) {
    Column(
        modifier = Modifier.width(width = 100.dp),
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
fun TeamInfoScore(fixtureData: FixtureData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${fixtureData.homeTeamScore} - ${fixtureData.awayTeamScore}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "90:15",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

data class Statistics(
    val shooting: Int,
    val attacks: Int,
    val possession: Int,
    val cards: Int,
    val corners: Int
)

data class Player(
    val id: Int,
    val name: String,
    val number: Int,
    val pos: String,
    val grid: String
)

data class LineUp(
    val formation: String,
    val players: List<Player>
)

data class EventData(
    val homeTeamStatistics: Statistics,
    val awayTeamStatistics: Statistics,
    val lineUp: LineUp
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MatchDetailsTabs() {
    val eventData = prepareEventData()
    val tabs = listOf(TabItem.MatchDetail(eventData), TabItem.LineUp(eventData), TabItem.H2H)
    val pagerState = rememberPagerState()
    Column() {
        Tabs(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    // OR ScrollableTabRow()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        contentColor = Color.White,
        indicator = {
            Box {}
        }) {
        tabs.forEachIndexed { index, tab ->
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
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState, count = tabs.size) { page ->
        tabs[page].screen()
    }
}

fun prepareEventData(): EventData {

    val lineUp = LineUp(formation = "4-3-3", players = getPlayers())
    val homeTeamStatistics =
        Statistics(shooting = 3, attacks = 12, possession = 42, cards = 3, corners = 5)
    val awayTeamStatistics =
        Statistics(shooting = 13, attacks = 25, possession = 58, cards = 1, corners = 9)
    return EventData(
        homeTeamStatistics = homeTeamStatistics,
        awayTeamStatistics = awayTeamStatistics,
        lineUp = lineUp
    )
}

//@Composable
//fun TextButtonTab(text: String, isActive: Boolean) {
//    val isActiveModifier = Modifier
//        .background(
//            brush = Brush.horizontalGradient(
//                colors = listOf(LightOrange, Orange)
//            )
//        )
//    TextButton(
//        onClick = { },
//        modifier = if (isActive) isActiveModifier else Modifier,
//        shape = if (isActive) RoundedCornerShape(10.dp) else RectangleShape
//    ) {
//        Text(text = text)
//    }
//}


fun getPlayers(): List<Player> {
    val player1 = Player(
        6258,
        "L. Pocrnjic",
        1,
        "G",
        "1:1"
    )
    val player2 = Player(
        6258,
        "L. Galeano",
        19,
        "D",
        "2:4"
    )
    val player3 = Player(
        6258,
        "M. Miers",
        6,
        "D",
        "2:3"
    )
    val player4 = Player(
        6258,
        "L. Villalba",
        21,
        "D",
        "2:2"
    )
    val player5 = Player(
        6258,
        "E. Iñíguez",
        5,
        "D",
        "2:1"
    )
    val player6 = Player(
        6258,
        "G. Gil",
        5,
        "M",
        "3:3"
    )
    val player7 = Player(
        6258,
        "F. Acevedo",
        8,
        "M",
        "3:2"
    )
    val player8 = Player(
        6258,
        "L. Maciel",
        33,
        "M",
        "3:1"
    )
    val player9 = Player(
        6258,
        "G. Verón",
        29,
        "F",
        "4:3"
    )
    val player10 = Player(
        6258,
        "F. Andrada",
        10,
        "F",
        "4:2"
    )
    val player11 = Player(
        6258,
        "N. Solís",
        7,
        "F",
        "4:1"
    )
    return listOf(
        player1,
        player2,
        player3,
        player4,
        player5,
        player6,
        player7,
        player8,
        player9,
        player10,
        player11
    )
}