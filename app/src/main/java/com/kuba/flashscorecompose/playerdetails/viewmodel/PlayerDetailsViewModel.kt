package com.kuba.flashscorecompose.playerdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.playerdetails.model.PlayerDetailsError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 01/02/2023.
 */
class PlayerDetailsViewModel(
    private val playerId: Int,
    private val team: Team,
    private val season: Int,
    private val playersRepository: PlayersDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(PlayerDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observePlayer()
    }

    fun refresh() {
        refreshPlayers()
    }

    private fun observePlayer() {
        viewModelScope.launch {
            playersRepository.observePlayer(playerId).collect { player ->
                if (player == null) {
                    viewModelState.update { it.copy(error = PlayerDetailsError.EmptyPlayer) }
                    return@collect
                }
                viewModelState.update { it.copy(player = player) }
            }
        }
    }

    private fun refreshPlayers() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = playersRepository.loadPlayer(playerId, team, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = PlayerDetailsError.RemoteError(result.error),
                        )
                    }
                }
            }
        }
    }
}