package com.kuba.flashscorecompose.fixturedetails.statistics.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.ui.theme.TextGreyLight
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FixtureCard
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent

/**
 * Created by jrzeznicki on 23/12/2022.
 */

private const val STATISTICS_KEY = "STATISTICS_KEY"

@Composable
fun StatisticsScreen(
    fixtureId: Int,
    leagueId: Int,
    round: String,
    navigator: DestinationsNavigator,
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
                is StatisticsUiState.HasData -> {
                    StatisticsRows(uiState.homeStatistics, uiState.awayStatistics)
                    Spacer(modifier = Modifier.size(24.dp))
                    OtherMatchHeader()
                    Spacer(modifier = Modifier.size(16.dp))
                    OtherFixtures(fixtures = uiState.fixtures, onFixtureClick = onFixtureClick)
                }
                is StatisticsUiState.NoData -> EmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    iconId = R.drawable.ic_close,
                    contentDescriptionId = R.string.load_data_from_network,
                    textId = R.string.no_statistics,
                    onRefreshClick = onRefreshClick
                )
            }
        }
    }
}

@Composable
private fun OtherFixtures(fixtures: List<FixtureItem>, onFixtureClick: (FixtureItem) -> Unit) {
    Column {
        fixtures.forEach { fixtureItem ->
            FixtureCard(fixtureItem = fixtureItem, onFixtureClick = onFixtureClick)
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

@Composable
private fun StatisticsRows(homeStatistics: Statistics, awayStatistics: Statistics) {
    val statisticsItems = homeStatistics.statistics.zip(awayStatistics.statistics)
    statisticsItems.forEach { (home, away) ->
        StatisticDetailRow(home.value, away.value, home.type)
    }
}

@Composable
private fun StatisticDetailRow(homeValue: String, awayValue: String, stat: String) {
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
private fun OtherMatchHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.other_matches),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            color = Color.White,
        )
        Text(
            text = stringResource(id = R.string.see_all),
            fontSize = 12.sp,
            color = TextGreyLight,
        )
    }
}