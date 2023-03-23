package com.example.leaguedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.fixture.repository.FixturesDataSource
import com.example.data.userpreferences.repository.UserPreferencesDataSource
import com.example.favoritefixtureinteractor.FavoriteFixtureInteractor
import com.example.leaguedetails.model.LeagueDetailsError
import com.example.model.fixture.FixtureItemWrapper
import com.example.model.league.League
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
class LeagueDetailsViewModel(
    private val league: League,
    private val fixturesRepository: FixturesDataSource,
    private val userPreferencesRepository: UserPreferencesDataSource,
    private val snackbarManager: SnackbarManager,
    private val favoriteFixtureInteractor: FavoriteFixtureInteractor
) : ViewModel() {
    private val viewModelState = MutableStateFlow(LeagueDetailsViewModelState())
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
            val fixturesFlow = fixturesRepository.observeFixtureByLeague(league.id)
            combine(flow = fixturesFlow, flow2 = userPreferencesFlow) { fixtures, userPreferences ->
                val favoriteFixtureIds = userPreferences.favoriteFixtureIds
                val fixtureItemWrappers = fixtures.map {
                    FixtureItemWrapper(
                        fixtureItem = it,
                        isFavorite = favoriteFixtureIds.contains(it.id)
                    )
                }
                val filteredFixtureItems =
                    filterFixtureItems(fixtureItemWrappers = fixtureItemWrappers)
                viewModelState.update {
                    it.copy(
                        fixtureItemWrappers = fixtureItemWrappers,
                        filteredFixtureItemWrappers = filteredFixtureItems
                    )
                }
            }.collect()
        }
    }

    fun changeDate(newDate: LocalDate) {
        viewModelState.update {
            it.copy(
                date = newDate,
                filteredFixtureItemWrappers = filterFixtureItems(date = newDate)
            )
        }
        refreshFixtures()
    }

    private fun filterFixtureItems(
        fixtureItemWrappers: List<FixtureItemWrapper> = viewModelState.value.fixtureItemWrappers,
        date: LocalDate = viewModelState.value.date
    ): List<FixtureItemWrapper> {
        return fixtureItemWrappers.filter { it.fixtureItem.fixture.shortDate == date.toString() }
    }

    private fun refreshFixtures() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val date = viewModelState.value.date
            val result =
                fixturesRepository.loadFixturesByDate(date.toString(), league.id, league.season)
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
                            error = LeagueDetailsError.RemoteError(result.error)
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
