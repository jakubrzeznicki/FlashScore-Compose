package com.kuba.flashscorecompose.fixturedetails.headtohead.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.RowScopeInstanceImpl.align
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.FixtureItemStyle
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.H2HType
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadUiState
import com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.theme.*
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * Created by jrzeznicki on 23/12/2022.
 */
private const val HEAD_TO_HEAD = "HEAD_TO_HEAD"

@Composable
fun HeadToHeadScreen(
    homeTeam: Team,
    awayTeam: Team,
    season: Int,
    fixtureId: Int,
    navigator: DestinationsNavigator,
    viewModel: HeadToHeadViewModel = getViewModel { parametersOf(homeTeam.id, awayTeam.id, season) }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = HEAD_TO_HEAD) { viewModel.setup() }
    HeadToHeadList(
        homeTeam,
        awayTeam,
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(fixtureId)) }
    )
}

@Composable
fun HeadToHeadList(
    homeTeam: Team,
    awayTeam: Team,
    uiState: HeadToHeadUiState,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit
) {
    val scrollState = rememberScrollState()
    LoadingContent(
        empty = when (uiState) {
            is HeadToHeadUiState.HasData -> false
            else -> uiState.isLoading
        }, emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.background)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is HeadToHeadUiState.HasData -> LastMatchesSection(
                    fixtures = uiState.homeTeamFixtures,
                    type = H2HType.LastFirstTeam(homeTeam.name),
                    onFixtureClick = onFixtureClick
                )
                else -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.load_data_from_network,
                    textId = R.string.no_last_fixtures,
                    onRefreshClick = onRefreshClick
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            when (uiState) {
                is HeadToHeadUiState.HasData -> LastMatchesSection(
                    fixtures = uiState.awayTeamFixtures,
                    type = H2HType.LastSecondTeam(awayTeam.name),
                    onFixtureClick = onFixtureClick
                )
                else -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.load_data_from_network,
                    textId = R.string.no_last_fixtures,
                    onRefreshClick = onRefreshClick
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            when (uiState) {
                is HeadToHeadUiState.HasData -> LastMatchesSection(
                    fixtures = uiState.h2hFixtures,
                    type = H2HType.HeadToHead,
                    onFixtureClick = onFixtureClick
                )
                else -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.load_data_from_network,
                    textId = R.string.no_fixtures_and_countries,
                    onRefreshClick = onRefreshClick
                )
            }
        }
    }
}

@Composable
fun LastMatchesSection(
    fixtures: List<FixtureItem>,
    type: H2HType,
    onFixtureClick: (FixtureItem) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "${stringResource(id = type.titleTextId)}: ${type.name}",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            fixtures.forEach {
                HeadToHeadMatchItem(fixtureItem = it, onFixtureClick)
                Divider(
                    color = GreyDark,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun HeadToHeadMatchItem(fixtureItem: FixtureItem, onFixtureClick: (FixtureItem) -> Unit) {
    val formattedDate = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(fixtureItem.fixture.timestamp.toLong()),
        TimeZone.getDefault().toZoneId()
    )
    val fixtureItemStyle = fixtureItemStyling(fixtureItem = fixtureItem)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = formattedDate.year.toString(),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                TeamInfo(
                    teamLogo = fixtureItem.homeTeam.logo,
                    teamName = fixtureItem.homeTeam.name,
                    fontWeight = fixtureItemStyle.first.fontWeight
                )
                TeamInfo(
                    teamLogo = fixtureItem.awayTeam.logo,
                    teamName = fixtureItem.awayTeam.name,
                    fontWeight = fixtureItemStyle.second.fontWeight
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FixtureScore(fixtureItemStyle, fixtureItem)
            Spacer(modifier = Modifier.size(20.dp))
            FixtureResultIcon(fixtureItemStyle)
        }
    }
}

@Composable
private fun TeamInfo(teamLogo: String, teamName: String, fontWeight: FontWeight) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier.size(24.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(teamLogo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = teamName,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fontWeight
            ),
            color = Color.White
        )
    }
}

@Composable
private fun FixtureScore(
    fixtureItemStyle: Pair<FixtureItemStyle, FixtureItemStyle>,
    fixtureItem: FixtureItem
) {
    Column {
        Text(
            text = fixtureItem.goals.home.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fixtureItemStyle.first.fontWeight
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = fixtureItem.goals.away.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fixtureItemStyle.second.fontWeight
            ),
            color = Color.White
        )
    }
}

@Composable
private fun FixtureResultIcon(fixtureItemStyle: Pair<FixtureItemStyle, FixtureItemStyle>) {
    Box(
        modifier = Modifier
            .background(
                color = fixtureItemStyle.first.color,
                shape = RoundedCornerShape(8.dp)
            )
            .size(24.dp)
            .align(Alignment.CenterVertically)
            .padding(4.dp)
    ) {
        Text(
            text = fixtureItemStyle.first.winnerText,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold
            ),
            color = Color.White
        )
    }
}

private fun fixtureItemStyling(fixtureItem: FixtureItem): Pair<FixtureItemStyle, FixtureItemStyle> {
    val homeGoals = fixtureItem.goals.home
    val awayGoals = fixtureItem.goals.away
    return if (homeGoals > awayGoals) {
        FixtureItemStyle(FontWeight.Bold, GreenLight, "W") to FixtureItemStyle(
            FontWeight.Normal,
            Red800,
            "W"
        )
    } else if (awayGoals > homeGoals) {
        FixtureItemStyle(FontWeight.Normal, Color.Red, "L") to FixtureItemStyle(
            FontWeight.Bold,
            GreenLight,
            "L"
        )
    } else {
        FixtureItemStyle(FontWeight.SemiBold, Blue500, "D") to FixtureItemStyle(
            FontWeight.SemiBold,
            Blue500,
            "D"
        )
    }
}