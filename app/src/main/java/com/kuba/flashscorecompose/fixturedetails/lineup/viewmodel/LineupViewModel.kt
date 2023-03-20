package com.kuba.flashscorecompose.fixturedetails.lineup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.lineups.LineupsDataSource
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.fixturedetails.lineup.model.LineupError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 13/01/2023.
 */
class LineupViewModel(
    private val fixtureId: Int,
    private val leagueId: Int,
    private val season: Int,
    private val lineupsRepository: LineupsDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LineupViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        // loadLineups()
        observeLineups()
    }

    fun refresh() {
        loadLineups()
    }

    private fun observeLineups() {
        viewModelScope.launch {
            lineupsRepository.observeLineups(fixtureId).collect { lineups ->
                val updatedLineups = lineups.map {
                    it.copy(
                        startXIWithPosition = it.startXI
                            .map { player ->
                                val names = player.name.split(SPACE)
                                val newName = if (names.size >= 2) {
                                    "${names.first().first()}.${names.last()}"
                                } else {
                                    player.name
                                }
                                player.copy(name = newName)
                            }
                            .groupBy { player ->
                                if (player.grid.isNotBlank()) {
                                    player.grid.firstOrNull().toString().toInt()
                                } else {
                                    0
                                }
                            }
                    )
                }
                val selectedLineup = getLineup(updatedLineups)
                viewModelState.update {
                    it.copy(
                        lineups = updatedLineups,
                        selectedLineup = selectedLineup
                    )
                }
            }
        }
    }

    fun changeSelectedLineup(newSelectedLineup: Lineup) {
        viewModelState.update {
            it.copy(selectedLineup = newSelectedLineup)
        }
    }

    private fun getLineup(
        lineups: List<Lineup> = viewModelState.value.lineups,
        selectedLineup: Lineup = viewModelState.value.selectedLineup
    ): Lineup {
        return if (selectedLineup.team == lineups.firstOrNull()?.team) {
            lineups.firstOrNull() ?: Lineup.EMPTY_LINEUP
        } else {
            lineups.lastOrNull() ?: Lineup.EMPTY_LINEUP
        }
    }

    private fun loadLineups() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = lineupsRepository.loadLineups(fixtureId, leagueId, season)
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
                            error = LineupError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private companion object {
        const val SPACE = " "
    }
}
