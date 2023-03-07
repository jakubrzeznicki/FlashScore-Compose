package com.kuba.flashscorecompose.fixturedetails.statistics.screen

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsUiState
import com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel.StatisticsViewModel
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FixtureCard
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = STATISTICS_KEY) { viewModel.setup() }
    StatisticsList(
        uiState = uiState,
        context = context,
        onRefreshClick = { viewModel.refresh() },
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.fixtureItem)) },
        onFavoriteClick = { viewModel.addFixtureToFavorite(it) }
    )
}

@Composable
private fun StatisticsList(
    uiState: StatisticsUiState,
    onRefreshClick: () -> Unit,
    context: Context,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFavoriteClick: (FixtureItemWrapper) -> Unit
) {
    val scrollState = rememberLazyListState()
    LoadingContent(
        empty = when (uiState) {
            is StatisticsUiState.HasAllData -> false
            is StatisticsUiState.HasOnlyStatistics -> false
            is StatisticsUiState.HasOnlyOtherFixtures -> false
            else -> uiState.isLoading
        }, emptyContent = { FullScreenLoading() },
        loading = uiState.isLoading,
        onRefresh = onRefreshClick
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            state = scrollState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is StatisticsUiState.HasAllData -> {
                    items(items = uiState.statistics) { (home, away) ->
                        StatisticDetailRow(home.value, away.value, home.type)
                    }
                    item {
                        OtherMatchesHeader()
                    }
                    items(items = uiState.fixtureItemWrappers) {
                        FixtureCard(
                            fixtureItemWrapper = it,
                            context = context,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }
                is StatisticsUiState.HasOnlyStatistics -> {
                    items(items = uiState.statistics) { (home, away) ->
                        StatisticDetailRow(home.value, away.value, home.type)
                    }
                }
                is StatisticsUiState.HasOnlyOtherFixtures -> {
                    item {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_statistics
                        )
                    }
                    item {
                        OtherMatchesHeader()
                    }
                    items(items = uiState.fixtureItemWrappers) {
                        FixtureCard(
                            fixtureItemWrapper = it,
                            context = context,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }
                is StatisticsUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            textId = R.string.no_loaded_statistics
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(8.dp),
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        }
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
            .padding(vertical = 4.dp, horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Text(
            text = homeValue,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            textAlign = TextAlign.Center
        )
        Text(
            text = awayValue,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondary,
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
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Text(
            text = stringResource(id = R.string.see_all),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}