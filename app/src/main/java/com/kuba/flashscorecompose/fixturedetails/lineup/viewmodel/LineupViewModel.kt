package com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.lineups.LineupsDataSource
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 13/01/2023.
 */
class LineupViewModel(
    private val fixtureId: Int,
    private val lineupsRepository: LineupsDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LineupViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        // loadLineups()
        observeLineups()
    }

    fun refresh() {
       // loadLineups()
    }

    private fun observeLineups() {
        viewModelScope.launch {
            lineupsRepository.observeLineups(fixtureId).collect { lineups ->
                viewModelState.update {
                    it.copy(homeLineup = lineups.firstOrNull(), awayLineup = lineups.lastOrNull())
                }
            }
        }
    }

    private fun loadLineups() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = lineupsRepository.loadLineups(fixtureId)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = LineupError.RemoteError(result.error)
                    )
                }
            }
        }
    }
}