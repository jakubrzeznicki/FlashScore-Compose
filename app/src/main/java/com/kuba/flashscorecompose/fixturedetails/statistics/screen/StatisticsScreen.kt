package com.kuba.flashscorecompose.fixturedetails.statistics.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.countries.screen.FullScreenLoading
import com.kuba.flashscorecompose.countries.screen.LoadingContent
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.home.screen.WidgetFixtures
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.ui.theme.TextGreyLight
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 23/12/2022.
 */

private const val STATISTICS_KEY = "STATISTICS_KEY"

@Composable
fun StatisticsScreen(
    fixtureId: Int,
    leagueId: Int,
    round: String,
    //navigator: DestinationsNavigator,
    viewModel: StatisticsViewModel = getViewModel { parametersOf(fixtureId, leagueId, round) },
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = STATISTICS_KEY) { viewModel.setup() }
    StatisticsList(uiState = uiState, onRefreshClick = { viewModel.refresh() }, onFixtureClick = {})
}

@Composable
fun StatisticsList(
    uiState: StatisticsUiState,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit
) {
    val scrollState = rememberScrollState()
    LoadingContent(
        empty = when (uiState) {
            is StatisticsUiState.HasData -> false
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
                is StatisticsUiState.HasData -> StatisticsRows(uiState.statistics)
                else -> EmptyStatisticsRows()
            }
            Spacer(modifier = Modifier.size(24.dp))
            OtherMatchHeader()
            Spacer(modifier = Modifier.size(16.dp))
            when (uiState) {
                is StatisticsUiState.HasData -> {
                    WidgetFixtures(
                        Modifier,
                        uiState.fixtures,
                        onFixtureClick,
                        { }
                    )
                }
                else -> EmptyFixtureWidget()
            }
        }
    }
}

@Composable
fun StatisticsRows(statistics: List<Statistics>) {
    val homeStatisticsItems = statistics.firstOrNull()?.statistics.orEmpty()
    val awayStatisticsItems = statistics.lastOrNull()?.statistics.orEmpty()
    val statisticsItems = homeStatisticsItems.zip(awayStatisticsItems)
    statisticsItems.forEach { (home, away) ->
        StatisticDetailRow(home.value, away.value, home.type)
    }
}

@Composable
fun StatisticDetailRow(homeValue: String, awayValue: String, stat: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(
            text = homeValue,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = stat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = TextGreyLight,
            textAlign = TextAlign.Center
        )
        Text(
            text = awayValue,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OtherMatchHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Other Match",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.White,
        )
        Text(
            text = "See all",
            fontSize = 12.sp,
            color = TextGreyLight,
        )
    }
}

//@Composable
//fun FixtureWidgets(fixtureWidgetsList: List<FixtureData>) {
//    Column {
//        fixtureWidgetsList.forEach {
//            //FixtureWidgetCard(fixtureData = it)
//            Spacer(modifier = Modifier.size(16.dp))
//        }
//    }
//}

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

@Composable
fun EmptyStatisticsRows() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            modifier = Modifier.size(40.dp),
            contentDescription = ""
        )
        Text("No Statistics", modifier = Modifier.padding(16.dp))
    }
}