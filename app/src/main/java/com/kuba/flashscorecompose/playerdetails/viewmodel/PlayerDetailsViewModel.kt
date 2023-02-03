package com.kuba.flashscorecompose.playerdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.playerdetails.model.PlayerDetailsError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 01/02/2023.
 */
class PlayerDetailsViewModel(
    private val playerId: Int,
    private val playersRepository: PlayersDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(PlayerDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observePlayer()
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

    fun cleanError() {
        viewModelState.update { it.copy(error = PlayerDetailsError.NoError) }
    }
}