package com.kuba.flashscorecompose.fixturedetails.headtohead.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.google.relay.compose.RowScopeInstanceImpl.align
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.FixtureItemStyle
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadUiState
import com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.theme.Blue
import com.kuba.flashscorecompose.ui.theme.GreenLight
import com.kuba.flashscorecompose.ui.theme.RedDark
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

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
        onFixtureClick = { fixtureItem ->
            navigator.navigate(FixtureDetailsRouteDestination(fixtureItem.id))
        }
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
    val scrollState = rememberLazyListState()
    LoadingContent(
        empty = when (uiState) {
            is HeadToHeadUiState.HasData -> false
            else -> uiState.isLoading
        }, emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            state = scrollState,
            horizontalAlignment = Alignment.Start
        ) {
            when (uiState) {
                is HeadToHeadUiState.HasData -> {
                    item {
                        MatchesHeaderText(
                            modifier = Modifier.padding(bottom = 8.dp),
                            title = "${stringResource(id = R.string.last_matches)}: ${homeTeam.name}"
                        )
                    }
                    items(items = uiState.homeTeamFixtures) {
                        LastMatchesSection(fixture = it, onFixtureClick = onFixtureClick)
                    }
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                        MatchesHeaderText(
                            modifier = Modifier.padding(bottom = 24.dp),
                            title = "${stringResource(id = R.string.last_matches)}: ${awayTeam.name}"
                        )
                    }
                    items(items = uiState.awayTeamFixtures) {
                        LastMatchesSection(fixture = it, onFixtureClick = onFixtureClick)
                    }
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                        MatchesHeaderText(
                            modifier = Modifier.padding(bottom = 8.dp),
                            title = stringResource(id = R.string.between)
                        )
                    }
                    items(items = uiState.h2hFixtures) {
                        LastMatchesSection(fixture = it, onFixtureClick = onFixtureClick)
                    }
                }
                is HeadToHeadUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_last_fixtures
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchesHeaderText(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = modifier,
        text = title,
        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSecondary,
        textAlign = TextAlign.Start
    )
}

@Composable
private fun LastMatchesSection(
    fixture: FixtureItem,
    onFixtureClick: (FixtureItem) -> Unit
) {
    MatchItem(fixtureItem = fixture, onFixtureClick)
    Divider(
        color = MaterialTheme.colorScheme.surface,
        thickness = 2.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun MatchItem(fixtureItem: FixtureItem, onFixtureClick: (FixtureItem) -> Unit) {
    val fixtureItemStyle = fixtureItemStyling(fixtureItem = fixtureItem)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFixtureClick(fixtureItem) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = fixtureItem.fixture.year,
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(end = 16.dp)
            )
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
            FixtureScore(modifier = Modifier.padding(end = 20.dp), fixtureItemStyle, fixtureItem)
            FixtureResultIcon(fixtureItemStyle)
        }
    }
}

@Composable
private fun TeamInfo(teamLogo: String, teamName: String, fontWeight: FontWeight) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .decoderFactory(SvgDecoder.Factory())
                .data(teamLogo)
                .size(Size.ORIGINAL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = teamName,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fontWeight
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun FixtureScore(
    modifier: Modifier,
    fixtureItemStyle: Pair<FixtureItemStyle, FixtureItemStyle>,
    fixtureItem: FixtureItem
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = fixtureItem.goals.home.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fixtureItemStyle.first.fontWeight
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = fixtureItem.goals.away.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = fixtureItemStyle.second.fontWeight
            ),
            color = MaterialTheme.colorScheme.onSecondary
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
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

private fun fixtureItemStyling(fixtureItem: FixtureItem): Pair<FixtureItemStyle, FixtureItemStyle> {
    val homeGoals = fixtureItem.goals.home
    val awayGoals = fixtureItem.goals.away
    return if (homeGoals > awayGoals) {
        FixtureItemStyle(FontWeight.Bold, GreenLight, "W") to FixtureItemStyle(
            FontWeight.Normal,
            RedDark,
            "W"
        )
    } else if (awayGoals > homeGoals) {
        FixtureItemStyle(FontWeight.Normal, RedDark, "L") to FixtureItemStyle(
            FontWeight.Bold,
            GreenLight,
            "L"
        )
    } else {
        FixtureItemStyle(FontWeight.SemiBold, Blue, "D") to FixtureItemStyle(
            FontWeight.SemiBold,
            Blue,
            "D"
        )
    }
}