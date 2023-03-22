package com.example.leaguedetails.viewmodel

import com.example.leaguedetails.model.LeagueDetailsError
import com.example.leaguedetails.model.LeagueDetailsUiState
import com.example.model.fixture.FixtureItemWrapper
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
data class LeagueDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: LeagueDetailsError = LeagueDetailsError.NoError,
    val date: LocalDate = LocalDate.now(),
    val fixtureItemWrappers: List<FixtureItemWrapper> = emptyList(),
    val filteredFixtureItemWrappers: List<FixtureItemWrapper> = emptyList()
) {
    fun toUiState(): LeagueDetailsUiState = if (filteredFixtureItemWrappers.isNotEmpty()) {
        LeagueDetailsUiState.HasData(
            isLoading = isLoading,
            error = error,
            date = date,
            fixtureItemWrappers = filteredFixtureItemWrappers
        )
    } else {
        LeagueDetailsUiState.NoData(
            isLoading = isLoading,
            error = error,
            date = date
        )
    }
}
