package com.kuba.flashscorecompose.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
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
import com.google.relay.compose.BoxScopeInstanceImpl.matchParentSize
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.CountryListScreenDestination
import com.kuba.flashscorecompose.destinations.MatchDetailsContainerScreenDestination
import com.kuba.flashscorecompose.ui.component.AppTopBar
import com.kuba.flashscorecompose.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Created by jrzeznicki on 21/12/2022.
 */
@Destination(route = "home", start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Scaffold(topBar = { TopBar(context = context) }, scaffoldState = scaffoldState) {
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
            CountryWidgetsList(navigator = navigator, modifier = Modifier)
            Spacer(modifier = Modifier.size(16.dp))
            HeaderWidgetLeagueCard(
                leagueWidgetItem = LeagueWidgetItem(
                    name = "Serie A",
                    logo = "https://media-5.api-sports.io/football/leagues/135.png",
                    flag = "https://media-5.api-sports.io/flags/it.svg",
                    countryName = "Italy",
                    isActive = true
                )
            )
            Spacer(modifier = Modifier.size(16.dp))
            FixtureWidgetCard(
                FixtureData(
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
                ),
                navigator
            )
        }
    }
}

@Composable
fun TopBar(context: Context) {
    AppTopBar(
        title = {
            Text(text = stringResource(id = R.string.live_score))
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { Toast.makeText(context, "Search", Toast.LENGTH_SHORT) }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            }
            IconButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .size(24.dp),
                onClick = { Toast.makeText(context, "Notifications", Toast.LENGTH_SHORT) }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "notifications"
                )
            }
        }
    )
}


@Composable
fun Banner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Blue500, Blue800),
                ),
                shape = RoundedCornerShape(16.dp)
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
                text = stringResource(id = R.string.text_second_banner),
                fontSize = 12.sp
            )
        }
        Image(
            painter = painterResource(id = R.drawable.salah_liverpool),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}

data class LeagueWidgetItem(
    val name: String,
    val logo: String,
    val flag: String,
    val countryName: String,
    val isActive: Boolean
)

@Composable
fun CountryWidgetsList(
    leagueWidgets: List<LeagueWidgetItem> = listOf(
        LeagueWidgetItem(
            name = "Serie A",
            logo = "https://media-5.api-sports.io/football/leagues/135.png",
            flag = "https://media-5.api-sports.io/flags/it.svg",
            countryName = "Italy",
            isActive = true
        ),
        LeagueWidgetItem(
            name = "Premier League",
            logo = "https://media-6.api-sports.io/football/leagues/39.png",
            flag = "https://media-5.api-sports.io/flags/gb.svg",
            countryName = "England",
            isActive = false
        ),
        LeagueWidgetItem(
            name = "Ekstraklasa",
            logo = "https://media-6.api-sports.io/football/leagues/106.png",
            flag = "https://media-5.api-sports.io/flags/pl.svg",
            countryName = "Poland",
            isActive = false
        ),
        LeagueWidgetItem(
            name = "Bundesliga",
            logo = "https://media-5.api-sports.io/football/leagues/78.png",
            flag = "https://media-5.api-sports.io/flags/de.svg",
            countryName = "Germany",
            isActive = false
        )
    ),
    navigator: DestinationsNavigator,
    modifier: Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyRow(modifier = modifier, state = state) {
        item {
            Row {
                leagueWidgets.forEach {
                    CountryWidgetCard(leagueWidgetItem = it, navigator = navigator)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryWidgetCard(leagueWidgetItem: LeagueWidgetItem, navigator: DestinationsNavigator) {
    Card(
        onClick = { navigator.navigate(CountryListScreenDestination()) },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.size(width = 115.dp, height = 130.dp),
    ) {
        val isActiveModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp),
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        LightOrange,
                        Orange
                    )
                )
            )
        val normalModifier = Modifier
            .padding(PaddingValues(7.dp))
            .background(
                shape = RoundedCornerShape(16.dp),
                color = Dark500
            )
        Column(
            modifier = if (leagueWidgetItem.isActive) isActiveModifier else normalModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                modifier = Modifier.size(40.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .decoderFactory(SvgDecoder.Factory())
                    .data(leagueWidgetItem.flag)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = leagueWidgetItem.countryName,
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
fun HeaderWidgetLeagueCard(leagueWidgetItem: LeagueWidgetItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                    .data(leagueWidgetItem.flag)
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
                    text = leagueWidgetItem.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White,
                )
                Text(
                    text = leagueWidgetItem.countryName,
                    fontSize = 12.sp,
                    color = Grey,
                )
            }
        }
        IconButton(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(16.dp),
            onClick = { }) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "")
        }
    }
}

data class Team(val name: String, val logo: String)
data class FixtureData(
    val homeTeam: Team,
    val awayTeam: Team,
    val homeTeamScore: Int,
    val awayTeamScore: Int
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FixtureWidgetCard(fixtureData: FixtureData, navigator: DestinationsNavigator? = null) {
    Card(
        onClick = { navigator?.navigate(MatchDetailsContainerScreenDestination()) },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = GreyLight
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(PaddingValues(16.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(shape = RoundedCornerShape(30.dp), color = GreyDark)
                            .size(40.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = PaddingValues(8.dp)),
                            model = ImageRequest.Builder(LocalContext.current)
                                .decoderFactory(SvgDecoder.Factory())
                                .data(fixtureData.homeTeam.logo)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(shape = RoundedCornerShape(30.dp), color = GreyDark)
                            .size(40.dp)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues = PaddingValues(8.dp)),
                            model = ImageRequest.Builder(LocalContext.current)
                                .decoderFactory(SvgDecoder.Factory())
                                .data(fixtureData.awayTeam.logo)
                                .size(Size.ORIGINAL)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = fixtureData.homeTeam.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = fixtureData.homeTeamScore.toString(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "vs",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                        Text(
                            text = "-",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = fixtureData.awayTeam.name.take(10),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = fixtureData.awayTeamScore.toString(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.White,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                        color = GreyDark
                    )
                    .size(70.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FT",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }

        }

    }
}

@Preview
@Composable
fun test() {
    AsyncImage(
        modifier = Modifier
            .size(60.dp)
            //.clip(RoundedCornerShape(size = 50.dp))
            .border(width = 30.dp, color = Color.Blue, shape = RoundedCornerShape(size = 50.dp)),
        model = ImageRequest.Builder(LocalContext.current)
            .decoderFactory(SvgDecoder.Factory())
            .data("https://media.api-sports.io/football/teams/33.png")
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build(),
        //placeholder = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = null,
        contentScale = ContentScale.Inside
    )
}