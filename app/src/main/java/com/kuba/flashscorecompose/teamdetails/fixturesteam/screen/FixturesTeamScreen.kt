package com.kuba.flashscorecompose.teamdetails.fixturesteam.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.destinations.FixtureDetailsRouteDestination
import com.kuba.flashscorecompose.teamdetails.fixturesteam.model.FixturesTeamUiState
import com.kuba.flashscorecompose.teamdetails.fixturesteam.viewmodel.FixturesTeamViewModel
import com.kuba.flashscorecompose.ui.component.*
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
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
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = SETUP_FIXTURES_TEAM_KEY) { viewModel.setup() }
    FixturesTeamListScreen(
        uiState = uiState,
        navigator = navigator,
        onFixtureClick = { navigator.navigate(FixtureDetailsRouteDestination(it.id)) },
        onFixturesFilterClick = { viewModel.filterFixtures(it) },
        onRefreshClick = { viewModel.refresh() }
    )
}

@Composable
fun FixturesTeamListScreen(
    uiState: FixturesTeamUiState,
    navigator: DestinationsNavigator,
    onFixtureClick: (FixtureItem) -> Unit,
    onFixturesFilterClick: (FilterChip.Fixtures) -> Unit,
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
                .padding(vertical = 16.dp),
            state = scrollState
        ) {
            when (uiState) {
                is FixturesTeamUiState.HasData -> {
                    item {
                        FixturesFilterChips(
                            uiState.fixtureFilterChip,
                            onFixturesFilterClick
                        )
                    }
                    items(items = uiState.fixtureItems) {
                        FixtureCard(fixtureItem = it, onFixtureClick = onFixtureClick)
                    }
                }
                is FixturesTeamUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_fixtures
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
    onFixturesFilterClick: (FilterChip.Fixtures) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        FilterFixturesChip(
            fixturesFilterChip = FilterChip.Fixtures.Played,
            isSelected = fixturesFilterChip is FilterChip.Fixtures.Played,
            onStateChanged = onFixturesFilterClick,
            icon = Icons.Filled.EventAvailable
        )
        FilterFixturesChip(
            fixturesFilterChip = FilterChip.Fixtures.Live,
            isSelected = fixturesFilterChip is FilterChip.Fixtures.Live,
            onStateChanged = onFixturesFilterClick,
            icon = Icons.Filled.LiveTv
        )
        FilterFixturesChip(
            fixturesFilterChip = FilterChip.Fixtures.Upcoming,
            isSelected = fixturesFilterChip is FilterChip.Fixtures.Upcoming,
            onStateChanged = onFixturesFilterClick,
            icon = Icons.Filled.Upcoming
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun FilterFixturesChip(
    fixturesFilterChip: FilterChip.Fixtures = FilterChip.Fixtures.Upcoming,
    isSelected: Boolean = true,
    onStateChanged: (FilterChip.Fixtures) -> Unit = {},
    icon: ImageVector = Icons.Filled.Upcoming
) {
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            iconColor = MaterialTheme.colorScheme.onSecondary,
            labelColor = MaterialTheme.colorScheme.onSecondary,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.inverseOnSurface,
            selectedLeadingIconColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        label = {
            Text(
                text = stringResource(id = fixturesFilterChip.textId),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        },
        shape = RoundedCornerShape(50),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = icon,
                contentDescription = null
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.inverseSurface
        ),
        onClick = { onStateChanged(fixturesFilterChip) }
    )
}
