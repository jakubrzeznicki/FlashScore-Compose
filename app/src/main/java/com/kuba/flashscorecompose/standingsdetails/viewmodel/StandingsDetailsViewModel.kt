package com.kuba.flashscorecompose.standingsdetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.standings.StandingsDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 19/01/2023.
 */
class StandingsDetailsViewModel(
    private val leagueId: Int,
    private val season: Int,
    private val standingsRepository: StandingsDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StandingsDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        getStandings()
    }

    private fun getStandings() {
        viewModelScope.launch {
            val standing = standingsRepository.getStanding(leagueId, season)
            viewModelState.update {
                it.copy(standing = standing)
            }
        }
    }
}