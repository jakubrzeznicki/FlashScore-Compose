package com.example.leaguedetails.model

import com.example.model.fixture.FixtureItemWrapper
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
sealed interface LeagueDetailsUiState {
    val isLoading: Boolean
    val error: LeagueDetailsError
    val date: LocalDate

    data class HasData(
        override val isLoading: Boolean,
        override val error: LeagueDetailsError,
        override val date: LocalDate,
        val fixtureItemWrappers: List<FixtureItemWrapper>
    ) : LeagueDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: LeagueDetailsError,
        override val date: LocalDate
    ) : LeagueDetailsUiState
}
