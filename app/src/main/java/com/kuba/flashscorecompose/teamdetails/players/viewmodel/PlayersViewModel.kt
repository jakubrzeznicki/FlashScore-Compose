package com.kuba.flashscorecompose.teamdetails.players.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerWrapper
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
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
    private val userPreferencesRepository: UserPreferencesDataSource
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
            val countriesFlow = countryRepository.observeCountries()
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val playersFlow = playersRepository.observePlayers(team.id, team.season)
            combine(
                flow = playersFlow,
                flow2 = countriesFlow,
                flow3 = userPreferencesFlow
            ) { players, countries, userPreferences ->
                val favoritePlayerIds = userPreferences.favoritePlayerIds
                val playerCountries = players.map {
                    PlayerWrapper(
                        player = it,
                        country = countries.firstOrNull { country -> country.name == it.nationality }
                            ?: Country.EMPTY_COUNTRY,
                        isFavorite = favoritePlayerIds.contains(it.id)
                    )
                }
                viewModelState.update {
                    it.copy(playerWrappers = playerCountries)
                }
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
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
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
            userPreferencesRepository.saveFavoritePlayerIds(favoritePlayerWrappers.map { it.player.id })
        }
    }
}