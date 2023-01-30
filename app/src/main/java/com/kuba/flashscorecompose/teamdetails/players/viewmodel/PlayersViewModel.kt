package com.kuba.flashscorecompose.teamdetails.players.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.players.PlayersDataSource
import com.kuba.flashscorecompose.teamdetails.players.model.PlayerCountry
import com.kuba.flashscorecompose.teamdetails.players.model.PlayersError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 29/01/2023.
 */
class PlayersViewModel(
    private val teamId: Int,
    private val season: Int,
    private val playersRepository: PlayersDataSource,
    private val countryRepository: CountryDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(PlayersViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observePlayers()
    }

    fun refresh() {
        refreshPlayers()
    }

    private fun observePlayers() {
        viewModelScope.launch {
            val countries = countryRepository.getCountries()
            playersRepository.observePlayers(teamId, season).collect { players ->
                viewModelState.update {
                    val playerCountry = players.map { player ->
                        PlayerCountry(
                            player = player,
                            country = countries.firstOrNull { country -> country.name == player.nationality }
                                ?: Country.EMPTY_COUNTRY)
                    }
                    it.copy(playerCountries = playerCountry)
                }
            }
        }
    }

    private fun refreshPlayers() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = playersRepository.loadPlayers(teamId, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = PlayersError.RemoteError(result.error),
                    )
                }
            }
        }
    }
}