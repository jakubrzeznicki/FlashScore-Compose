package com.example.fixturedetails.headtohead.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.fixturedetails.R
import com.example.fixturedetails.headtohead.model.FixtureItemStyle
import com.example.fixturedetails.headtohead.model.HeadToHeadUiState
import com.example.fixturedetails.headtohead.model.ScoreStyle
import com.example.fixturedetails.headtohead.model.StyledFixtureItem
import com.example.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.example.fixturedetails.navigation.FixtureDetailsNavigator
import com.example.model.fixture.FixtureItem
import com.example.model.team.Team
import com.example.ui.composables.EmptyState
import com.example.ui.composables.FullScreenLoading
import com.example.ui.composables.LoadingContent
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
    navigator: FixtureDetailsNavigator,
    viewModel: HeadToHeadViewModel = getViewModel { parametersOf(homeTeam.id, awayTeam.id, season) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = HEAD_TO_HEAD) { viewModel.setup() }
    HeadToHeadList(
        homeTeam,
        awayTeam,
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = {
            navigator.openFixtureDetails(it.id)
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
        },
        emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
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
                        LastMatchesSection(styledFixtureItems = it, onFixtureClick = onFixtureClick)
                    }
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                        MatchesHeaderText(
                            modifier = Modifier.padding(bottom = 24.dp),
                            title = "${stringResource(id = R.string.last_matches)}: ${awayTeam.name}"
                        )
                    }
                    items(items = uiState.awayTeamFixtures) {
                        LastMatchesSection(styledFixtureItems = it, onFixtureClick = onFixtureClick)
                    }
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                        MatchesHeaderText(
                            modifier = Modifier.padding(bottom = 8.dp),
                            title = stringResource(id = R.string.between)
                        )
                    }
                    items(items = uiState.h2hFixtures) {
                        LastMatchesSection(styledFixtureItems = it, onFixtureClick = onFixtureClick)
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
    styledFixtureItems: StyledFixtureItem,
    onFixtureClick: (FixtureItem) -> Unit
) {
    MatchItem(styledFixtureItem = styledFixtureItems, onFixtureClick)
    Divider(
        color = MaterialTheme.colorScheme.inverseSurface,
        thickness = 2.dp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun MatchItem(
    styledFixtureItem: StyledFixtureItem,
    onFixtureClick: (FixtureItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFixtureClick(styledFixtureItem.fixtureItem) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = styledFixtureItem.fixtureItem.fixture.year.toString(),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                TeamInfo(
                    teamLogo = styledFixtureItem.fixtureItem.homeTeam.logo,
                    teamName = styledFixtureItem.fixtureItem.homeTeam.name,
                    fontWeight = styledFixtureItem.fixtureItemStyle.homeTeamStyle.fontWeight
                )
                TeamInfo(
                    teamLogo = styledFixtureItem.fixtureItem.awayTeam.logo,
                    teamName = styledFixtureItem.fixtureItem.awayTeam.name,
                    fontWeight = styledFixtureItem.fixtureItemStyle.awayTeamStyle.fontWeight
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FixtureScore(
                modifier = Modifier.padding(end = 20.dp),
                styledFixtureItem.fixtureItemStyle.homeTeamStyle.fontWeight,
                styledFixtureItem.fixtureItemStyle.awayTeamStyle.fontWeight,
                styledFixtureItem.fixtureItem
            )
            FixtureResultIcons(styledFixtureItem.fixtureItemStyle)
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
            placeholder = painterResource(id = com.example.ui.R.drawable.ic_close),
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
    homeFontWeight: FontWeight,
    awayFontWeight: FontWeight,
    fixtureItem: FixtureItem
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = fixtureItem.goals.home.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = homeFontWeight
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = fixtureItem.goals.away.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = awayFontWeight
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun FixtureResultIcons(fixtureItemStyle: FixtureItemStyle) {
    Column {
        FixtureResultIcon(fixtureItemStyle.homeTeamStyle)
        Spacer(modifier = Modifier.size(4.dp))
        FixtureResultIcon(fixtureItemStyle.awayTeamStyle)
    }
}

@Composable
private fun FixtureResultIcon(scoreStyle: ScoreStyle) {
    Box(
        modifier = Modifier
            .background(
                color = scoreStyle.color,
                shape = RoundedCornerShape(2.dp)
            )
            .size(16.dp)
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = scoreStyle.text,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
