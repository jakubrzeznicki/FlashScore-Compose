package com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.StatisticsDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 12/01/2023.
 */
class StatisticsViewModel(
    private val fixtureId: Int,
    private val leagueId: Int,
    private val round: String,
    private val season: Int,
    private val statisticsRepository: StatisticsDataSource,
    private val fixturesRepository: FixturesDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StatisticsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        // loadStatistics()
        // loadFixtures()
        observeStatistics()
        observeFixtures()
    }

    fun refresh() {
        loadStatistics()
        loadFixtures()
    }

    private fun observeStatistics() {
        viewModelScope.launch {
            statisticsRepository.observeStatistics(fixtureId).collect { statistics ->
                val homeStatistics = statistics.firstOrNull() ?: Statistics.EMPTY_STATISTICS
                val awayStatistics = statistics.lastOrNull() ?: Statistics.EMPTY_STATISTICS
                viewModelState.update {
                    it.copy(
                        homeTeam = homeStatistics.team,
                        awayTeam = awayStatistics.team,
                        statistics = homeStatistics.statistics.zip(awayStatistics.statistics)
                    )
                }
            }
        }
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesFilteredByRound(leagueId, season, round)
                .collect { fixtures ->
                    viewModelState.update { it.copy(fixtures = fixtures) }
                }
        }
    }

    private fun loadStatistics() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = statisticsRepository.loadStatistics(fixtureId)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = StatisticsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    private fun loadFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesFilteredByRound(leagueId, season, round)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = StatisticsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }
}