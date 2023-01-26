package com.kuba.flashscorecompose.leaguedetails.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League
import java.time.LocalDate

/**
 * Created by jrzeznicki on 25/01/2023.
 */
sealed interface LeagueDetailsUiState {
    val isLoading: Boolean
    val error: LeagueDetailsError
    val date: LocalDate
    val league: League

    data class HasData(
        override val isLoading: Boolean,
        override val error: LeagueDetailsError,
        override val date: LocalDate,
        override val league: League,
        val fixtureItems: List<FixtureItem>,
    ) : LeagueDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: LeagueDetailsError,
        override val date: LocalDate,
        override val league: League
    ) : LeagueDetailsUiState
}