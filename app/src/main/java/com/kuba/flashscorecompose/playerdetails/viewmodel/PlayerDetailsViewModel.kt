package com.kuba.flashscorecompose.playerdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.playerdetails.model.PlayerDetailsError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
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
                            error = PlayerDetailsError.RemoteError(result.error),
                        )
                    }
                }
            }
        }
    }
}