package com.kuba.flashscorecompose.teamdetails.players.screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuba.flashscorecompose.R
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.destinations.PlayerDetailsRouteDestination
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersUiState
import com.kuba.flashscorecompose.teamdetails.players.viewmodel.PlayersViewModel
import com.kuba.flashscorecompose.ui.component.EmptyState
import com.kuba.flashscorecompose.ui.component.FullScreenLoading
import com.kuba.flashscorecompose.ui.component.LoadingContent
import com.kuba.flashscorecompose.ui.component.PlayerCard
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created by jrzeznicki on 29/01/2023.
 */
private const val PLAYERS_KEY = "PLAYERS_KEY"

@Composable
fun PlayersScreen(
    team: Team,
    season: Int,
    navigator: DestinationsNavigator,
    viewModel: PlayersViewModel = getViewModel { parametersOf(team, season) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = PLAYERS_KEY) { viewModel.setup() }
    PlayersListScreen(
        uiState = uiState,
        onPlayerClick = {
            navigator.navigate(
                PlayerDetailsRouteDestination(
                    it.player.id,
                    it.player.team,
                    it.player.season
                )
            )
        },
        onPlayerFavoriteClick = { viewModel.addPlayerToFavorite(it) },
        onRefreshClick = { viewModel.refresh() }
    )
}

@Composable
fun PlayersListScreen(
    uiState: PlayersUiState,
    onPlayerClick: (PlayerWrapper) -> Unit,
    onPlayerFavoriteClick: (PlayerWrapper) -> Unit,
    onRefreshClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    LoadingContent(
        empty = when (uiState) {
            is PlayersUiState.HasData -> false
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
                is PlayersUiState.HasData -> {
                    items(items = uiState.playerWrappers) { playerCountry ->
                        PlayerCard(playerCountry, onPlayerClick, onPlayerFavoriteClick)
                    }
                }
                is PlayersUiState.NoData -> {
                    item {
                        EmptyState(
                            modifier = Modifier.fillMaxWidth(),
                            textId = R.string.no_players
                        )
                    }
                }
            }
        }
    }
}