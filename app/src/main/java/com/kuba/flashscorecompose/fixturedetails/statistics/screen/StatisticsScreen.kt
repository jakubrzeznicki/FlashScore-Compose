package com.kuba.flashscorecompose.fixturedetails.statistics.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.ui.theme.TextGreyLight
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
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
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: StatisticsViewModel = getViewModel {
        parametersOf(
            fixtureId,
            leagueId,
            round,
            season
        )
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = STATISTICS_KEY) { viewModel.setup() }
    StatisticsList(
        uiState = uiState,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.id)) })
}

@Composable
fun StatisticsList(
    uiState: StatisticsUiState,
    onRefreshClick: () -> Unit,
    onFixtureClick: (FixtureItem) -> Unit
) {
    val scrollState = rememberLazyListState()
    LoadingContent(
        empty = when (uiState) {
            is StatisticsUiState.HasData -> false
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is StatisticsUiState.HasData -> {
                    items(items = uiState.statistics) { (home, away) ->
                        StatisticDetailRow(home.value, away.value, home.type)
                    }
                    item {
                        OtherMatchesHeader()
                    }
                    items(items = uiState.fixtures) {
                        FixtureCard(fixtureItem = it, onFixtureClick = onFixtureClick)
                    }
                }
                is StatisticsUiState.NoData -> {
                    item {
                        EmptyState(
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
private fun OtherMatchesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
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