package com.kuba.flashscorecompose.teamdetails.fixturesteam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.teamdetails.fixturesteam.model.FixturesTeamError
import com.kuba.flashscorecompose.ui.component.chips.FilterChip
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager.showSnackbarMessage
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 30/01/2023.
 */
class FixturesTeamViewModel(
    private val teamId: Int,
    private val season: Int,
    private val fixturesRepository: FixturesDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(FixturesTeamViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        observeFixtures()
    }

    fun refresh() {
        refreshFixtures()
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            fixturesRepository.observeFixturesByTeam(teamId, season).collect { fixtureItems ->
                val filteredFixtures = filterFixtures(fixtureItems = fixtureItems)
                viewModelState.update {
                    it.copy(fixtures = fixtureItems, filteredFixtures = filteredFixtures)
                }
            }
        }
    }

    fun filterFixtures(newFixturesFilterChip: FilterChip.Fixtures) {
        val filteredFixtureItems = filterFixtures(fixturesFilterChip = newFixturesFilterChip)
        viewModelState.update {
            it.copy(
                filteredFixtures = filteredFixtureItems,
                fixtureFilterChip = newFixturesFilterChip
            )
        }
    }

    private fun filterFixtures(
        fixtureItems: List<FixtureItem> = viewModelState.value.fixtures,
        fixturesFilterChip: FilterChip.Fixtures = viewModelState.value.fixtureFilterChip
    ): List<FixtureItem> {
        return when (fixturesFilterChip) {
            is FilterChip.Fixtures.Upcoming ->
                fixtureItems.filter { it.fixture.periods.first == 0 }
            is FilterChip.Fixtures.Played ->
                fixtureItems.filter { it.fixture.periods.second != 0 }
            is FilterChip.Fixtures.Live ->
                fixtureItems.filter { it.fixture.periods.first != 0 && it.fixture.periods.second == 0 }
        }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixturesByTeam(teamId = teamId, season = season)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        result.error.statusMessage?.showSnackbarMessage(SnackbarMessageType.Error)
                        it.copy(
                            isLoading = false,
                            error = FixturesTeamError.RemoteError(result.error),
                        )
                    }
                }
            }
        }
    }
}