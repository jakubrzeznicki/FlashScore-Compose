package com.kuba.flashscorecompose.fixturedetails.statistics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.StatisticsDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.model.Statistics
import com.kuba.flashscorecompose.data.userpreferences.UserPreferencesDataSource
import com.kuba.flashscorecompose.fixturedetails.statistics.model.StatisticsError
import com.kuba.flashscorecompose.home.interactor.FavoriteFixtureInteractor
import com.kuba.flashscorecompose.home.model.FixtureItemWrapper
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
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
    private val fixturesRepository: FixturesDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val favoriteFixtureInteractor: FavoriteFixtureInteractor,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(StatisticsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

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
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val fixturesFlow =
                fixturesRepository.observeFixturesFilteredByRound(leagueId, season, round)
            combine(flow = fixturesFlow, flow2 = userPreferencesFlow) { fixtures, userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                val fixtureItemWrappers = fixtures.map {
                    FixtureItemWrapper(
                        fixtureItem = it,
                        isFavorite = favoriteFixtureIds.contains(it.id)
                    )
                }
                viewModelState.update { it.copy(fixtureItemWrappers = fixtureItemWrappers) }
            }.collect()
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = StatisticsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }

    fun addFixtureToFavorite(fixtureItemWrapper: FixtureItemWrapper) {
        viewModelScope.launch {
            favoriteFixtureInteractor.addFixtureToFavorite(fixtureItemWrapper)
        }
    }
}
