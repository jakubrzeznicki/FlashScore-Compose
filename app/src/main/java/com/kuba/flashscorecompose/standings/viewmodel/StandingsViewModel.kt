package com.kuba.flashscorecompose.standings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.standings.StandingsRepository
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsViewModel(
    private val standingsRepository: StandingsRepository,
    private val leagueIds: List<Int>,
    private val season: Int
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StandingsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeStandings()
        refreshStandings()
    }

    fun refreshStandings() {
        leagueIds.forEach { leagueId ->
            refreshStanding(leagueId)
        }
    }

    private fun refreshStanding(leagueId: Int) {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = standingsRepository.loadStandings(leagueId, season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = StandingsError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    private fun observeStandings() {
        viewModelScope.launch {
            standingsRepository.observeStandings(leagueIds, season).collect { standings ->
                viewModelState.update {
                    it.copy(standings = standings)
                }
            }
        }
    }
}