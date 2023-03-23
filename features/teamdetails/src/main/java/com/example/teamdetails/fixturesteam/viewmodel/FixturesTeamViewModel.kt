package com.example.teamdetails.fixturesteam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.fixture.repository.FixturesDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.favoritefixtureinteractor.FavoriteFixtureInteractor
import com.example.model.fixture.FixtureItemWrapper
import com.example.teamdetails.fixturesteam.model.FixturesTeamError
import com.example.ui.composables.chips.FilterChip
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 30/01/2023.
 */
class FixturesTeamViewModel(
    private val teamId: Int,
    private val season: Int,
    private val fixturesRepository: FixturesDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val favoriteFixtureInteractor: FavoriteFixtureInteractor,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(FixturesTeamViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeFixtures()
        //refreshFixtures()
    }

    fun refresh() {
        refreshFixtures()
    }

    private fun observeFixtures() {
        viewModelScope.launch {
            val currentUserId = userPreferencesRepository.getCurrentUserId()
            val userPreferencesFlow =
                userPreferencesRepository.observeUserPreferences(currentUserId)
            val fixturesFlow = fixturesRepository.observeFixturesByTeam(teamId, season)
            combine(flow = fixturesFlow, flow2 = userPreferencesFlow) { fixtures, userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                val fixtureItemWrappers = fixtures.map {
                    FixtureItemWrapper(
                        fixtureItem = it,
                        isFavorite = favoriteFixtureIds.contains(it.id)
                    )
                }
                val filteredFixtures = filterFixtures(fixtureItemWrappers = fixtureItemWrappers)
                viewModelState.update {
                    it.copy(
                        fixtureItemWrappers = fixtureItemWrappers,
                        filteredFixtureItemWrappers = filteredFixtures
                    )
                }
            }.collect()
        }
    }

    fun filterFixtures(newFixturesFilterChip: FilterChip.Fixtures) {
        val filteredFixtureItems = filterFixtures(fixturesFilterChip = newFixturesFilterChip)
        viewModelState.update {
            it.copy(
                filteredFixtureItemWrappers = filteredFixtureItems,
                fixtureFilterChip = newFixturesFilterChip
            )
        }
    }

    private fun filterFixtures(
        fixtureItemWrappers: List<FixtureItemWrapper> = viewModelState.value.fixtureItemWrappers,
        fixturesFilterChip: FilterChip.Fixtures = viewModelState.value.fixtureFilterChip
    ): List<FixtureItemWrapper> {
        return when (fixturesFilterChip) {
            is FilterChip.Fixtures.Upcoming ->
                fixtureItemWrappers.filter { it.fixtureItem.fixture.periods.first == 0 }
            is FilterChip.Fixtures.Played ->
                fixtureItemWrappers.filter { it.fixtureItem.fixture.periods.second != 0 }
            is FilterChip.Fixtures.Live ->
                fixtureItemWrappers.filter {
                    it.fixtureItem.fixture.periods.first != 0 && it.fixtureItem.fixture.periods.second == 0
                }
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
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = FixturesTeamError.RemoteError(result.error)
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
