package com.kuba.flashscorecompose.teamdetails.players.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper.Companion.toPlayerWrappers
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersViewModel(
    private val team: Team,
    private val season: Int,
    private val playersRepository: PlayersDataSource,
    private val countryRepository: CountryDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(PlayersViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observePlayers()
    }

    fun refresh() {
        refreshPlayers()
    }

    private fun observePlayers() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val playersFlow = playersRepository.observePlayers(team.id, team.season)
            combine(
                flow = playersFlow,
                flow2 = userPreferencesFlow
            ) { players, userPreferences ->
                val favoritePlayerIds = userPreferences.favoritePlayerIds
                val countries = countryRepository.getCountries()
                val playerWrappers = players.toPlayerWrappers(countries, favoritePlayerIds)
                viewModelState.update { it.copy(playerWrappers = playerWrappers) }
            }.collect()
        }
    }

    private fun refreshPlayers() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = playersRepository.loadPlayers(team, season)
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
                            error = PlayersError.RemoteError(result.error),
                        )
                    }
                }
            }
        }
    }

    fun addPlayerToFavorite(playerWrapper: PlayerWrapper) {
        viewModelScope.launch {
            val favoritePlayerWrappers =
                viewModelState.value.playerWrappers.filter { it.isFavorite }.toMutableList()
            if (playerWrapper.isFavorite) {
                favoritePlayerWrappers.remove(playerWrapper)
            } else {
                favoritePlayerWrappers.add(playerWrapper.copy(isFavorite = true))
            }
            val playerWrapperIds = viewModelState.value.playerWrappers.map { it.player.id }
            val favoritePlayerIds =
                userPreferencesRepository.getUserPreferences()?.favoritePlayerIds.orEmpty()
            val othersFavoritePlayerIds =
                favoritePlayerIds.filterNot { playerWrapperIds.contains(it) }
            userPreferencesRepository.saveFavoritePlayerIds(
                othersFavoritePlayerIds + favoritePlayerWrappers.map { it.player.id }
            )
        }
    }
}