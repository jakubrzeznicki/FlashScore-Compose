package com.example.teamdetails.players.screen

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
import com.example.model.player.PlayerWrapper
import com.example.model.team.Team
import com.example.teamdetails.navigation.TeamDetailsNavigator
import com.example.teamdetails.players.model.PlayersUiState
import com.example.teamdetails.players.viewmodel.PlayersViewModel
import com.example.ui.composables.EmptyState
import com.example.ui.composables.FullScreenLoading
import com.example.ui.composables.LoadingContent
import com.example.ui.composables.PlayerCard
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
    navigator: TeamDetailsNavigator,
    viewModel: PlayersViewModel = getViewModel { parametersOf(team, season) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = PLAYERS_KEY) { viewModel.setup() }
    PlayersListScreen(
        uiState = uiState,
        onPlayerClick = {
            navigator.openPlayerDetails(it.player.id, it.player.team, it.player.season)
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
                            textId = com.example.ui.R.string.no_players
                        )
                    }
                }
            }
        }
    }
}
