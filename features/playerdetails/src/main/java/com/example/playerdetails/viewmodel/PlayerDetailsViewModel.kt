package com.example.playerdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.country.repository.CountryDataSource
import com.example.data.player.repository.PlayersDataSource
import com.example.model.team.Team
import com.example.playerdetails.model.PlayerDetailsError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 01/02/2023.
 */
class PlayerDetailsViewModel(
    private val playerId: Int,
    private val team: Team,
    private val season: Int,
    private val playersRepository: PlayersDataSource,
    private val countryRepository: CountryDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(PlayerDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observePlayer()
        //refreshPlayers()
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
                val country = countryRepository.getCountry(player.nationality)
                viewModelState.update { it.copy(player = player, country = country) }
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = PlayerDetailsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }
}
