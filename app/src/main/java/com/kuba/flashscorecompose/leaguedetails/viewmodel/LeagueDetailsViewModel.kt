package com.kuba.flashscorecompose.leaguedetails.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leaguedetails.model.LeagueDetailsError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
class LeagueDetailsViewModel(
    private val league: League,
    private val fixturesRepository: FixturesDataSource
) : ViewModel() {
    private val viewModelState = MutableStateFlow(LeagueDetailsViewModelState())
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
            fixturesRepository.observeFixtureByLeague(league.id)
                .collect { fixtureItems ->
                    val filteredFixtureItems = filterFixtureItems(fixtureItems = fixtureItems)
                    viewModelState.update {
                        it.copy(
                            fixtureItems = fixtureItems,
                            filteredFixtureItems = filteredFixtureItems
                        )
                    }
                }
        }
    }

    fun changeDate(newDate: LocalDate) {
        viewModelState.update {
            it.copy(
                date = newDate,
                filteredFixtureItems = filterFixtureItems(date = newDate)
            )
        }
        //refreshFixtures()
    }

    private fun filterFixtureItems(
        fixtureItems: List<FixtureItem> = viewModelState.value.fixtureItems,
        date: LocalDate = viewModelState.value.date
    ): List<FixtureItem> {
        return fixtureItems.filter { it.fixture.shortDate == date.toString() }
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
                    is RepositoryResult.Error -> it.copy(
                        isLoading = false,
                        error = LeagueDetailsError.RemoteError(result.error)
                    )
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = LeagueDetailsError.NoError) }
    }
}