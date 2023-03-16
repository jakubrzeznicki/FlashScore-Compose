package com.example.teamdetails.fixturesteam.screen

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.fixture.FixtureItemWrapper
import com.example.teamdetails.R
import com.example.teamdetails.fixturesteam.model.FixturesTeamUiState
import com.example.teamdetails.fixturesteam.viewmodel.FixturesTeamViewModel
import com.example.ui.composables.EmptyState
import com.example.ui.composables.FixtureCard
import com.example.ui.composables.FullScreenLoading
import com.example.ui.composables.LoadingContent
import com.example.ui.composables.chips.CustomFilterChip
import com.example.ui.composables.chips.FilterChip
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 30/01/2023.
 */

private const val SETUP_FIXTURES_TEAM_KEY = "SETUP_FIXTURES_TEAM_KEY"

@Composable
fun FixturesTeamScreen(
    teamId: Int,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: FixturesTeamViewModel = getViewModel { parametersOf(teamId, season) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(key1 = SETUP_FIXTURES_TEAM_KEY) { viewModel.setup() }
    FixturesTeamListScreen(
        uiState = uiState,
        context = context,
        onFixtureClick = {
           // navigator.navigate(FixtureDetailsRouteDestination(it.fixtureItem.id))
        },
        onFixtureFavoriteClick = { viewModel.addFixtureToFavorite(it) },
        onFixturesFilterClick = { viewModel.filterFixtures(it as FilterChip.Fixtures) },
        onRefreshClick = { viewModel.refresh() }
    )
}

@Composable
fun FixturesTeamListScreen(
    uiState: FixturesTeamUiState,
    context: Context,
    onFixtureClick: (FixtureItemWrapper) -> Unit,
    onFixtureFavoriteClick: (FixtureItemWrapper) -> Unit,
    onFixturesFilterClick: (FilterChip) -> Unit,
    onRefreshClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    LoadingContent(
        modifier = Modifier.padding(),
        empty = when (uiState) {
            is FixturesTeamUiState.HasData -> false
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
            state = scrollState
        ) {
            when (uiState) {
                is FixturesTeamUiState.HasData -> {
                    item {
                        FixturesFilterChips(
                            uiState.fixtureFilterChip,
                            uiState.fixtureFilterChips,
                            onFixturesFilterClick
                        )
                    }
                    items(items = uiState.fixtureItemWrappers) {
                        FixtureCard(
                            fixtureItemWrapper = it,
                            context = context,
                            onFixtureClick = onFixtureClick,
                            onFavoriteClick = onFixtureFavoriteClick
                        )
                    }
                }
                is FixturesTeamUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_fixtures_team
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FixturesFilterChips(
    fixturesFilterChip: FilterChip.Fixtures,
    fixturesFilterChips: List<FilterChip.Fixtures>,
    onFixturesFilterClick: (FilterChip) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        fixturesFilterChips.forEach {
            CustomFilterChip(
                filterChip = it,
                isSelected = it == fixturesFilterChip,
                onStateChanged = onFixturesFilterClick,
                icon = it.icon
            )
        }
    }
}
