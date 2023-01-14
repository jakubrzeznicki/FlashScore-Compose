package com.kuba.flashscorecompose.fixturedetails.headtohead.screen

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.fixturedetails.headtohead.model.HeadToHeadUiState
import com.kuba.flashscorecompose.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.kuba.flashscorecompose.fixturedetails.statistics.screen.EmptyFixtureWidget
import com.kuba.flashscorecompose.ui.theme.GreenLight
import com.kuba.flashscorecompose.ui.theme.GreyDark
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
    viewModel: HeadToHeadViewModel = getViewModel { parametersOf(homeTeam.id, awayTeam.id, season) }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = HEAD_TO_HEAD) { viewModel.setup() }
    HeadToHeadList(
        homeTeam,
        awayTeam,
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = {})
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
                is HeadToHeadUiState.HasData -> {
                    LastMatchesSection(
                        fixtures = uiState.homeTeamFixtures,
                        type = H2HType.LastFirstTeam(homeTeam.name)
                    )
                }
                else -> EmptyFixtureWidget()
            }
            Spacer(modifier = Modifier.size(16.dp))
            when (uiState) {
                is HeadToHeadUiState.HasData -> {
                    LastMatchesSection(
                        fixtures = uiState.awayTeamFixtures,
                        type = H2HType.LastSecondTeam(awayTeam.name)
                    )
                }
                else -> EmptyFixtureWidget()
            }
            Spacer(modifier = Modifier.size(16.dp))
            when (uiState) {
                is HeadToHeadUiState.HasData -> {
                    LastMatchesSection(
                        fixtures = uiState.h2hFixtures,
                        type = H2HType.HeadToHead(homeTeam, awayTeam)
                    )
                }
                else -> EmptyFixtureWidget()
            }
        }
    }
}

@Composable
fun LastMatchesSection(fixtures: List<FixtureItem>, type: H2HType) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "${type.title} ${type.name}$",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column() {
            fixtures.forEach {
                HeadToHeadMatchItem(fixtureData = it)
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
fun HeadToHeadMatchItem(fixtureData: FixtureItem) {
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
                text = "2022",
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
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
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = fixtureData.homeTeam.name,
                        style = TextStyle(fontSize = 12.sp),
                        color = Color.White
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
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
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = fixtureData.awayTeam.name,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                        color = Color.White
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = fixtureData.goals.home.toString(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = fixtureData.goals.away.toString(),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Box(
                modifier = Modifier
                    .background(color = GreenLight, shape = RoundedCornerShape(8.dp))
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(4.dp)
            ) {
                Text(
                    text = "Z",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 12.sp, fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

sealed class H2HType(val title: String, open val name: String) {
    class LastFirstTeam(override val name: String) : H2HType("Ostanie mecze ", name)
    class LastSecondTeam(override val name: String) : H2HType("Ostatnie mecze ", name)
    class HeadToHead(homeTeam: Team, awayTeam: Team) : H2HType("Bezpośrednie ", "")
}