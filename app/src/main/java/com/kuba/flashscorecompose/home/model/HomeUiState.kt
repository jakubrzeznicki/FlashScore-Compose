package com.kuba.flashscorecompose.home.model

import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 05/01/2023.
 */
sealed interface HomeUiState {
    val isLoading: Boolean
    val error: HomeError
    val searchQuery: String

    data class HasAllData(
        override val isLoading: Boolean,
        override val error: HomeError,
        override val searchQuery: String,
        val countries: List<Country>,
        val selectedCountry: Country,
        val leagueFixturesDataList: List<LeagueFixturesData>
    ) : HomeUiState

    data class HasOnlyCountries(
        override val isLoading: Boolean,
        override val error: HomeError,
        override val searchQuery: String,
        val selectedCountry: Country,
        val countries: List<Country>
    ) : HomeUiState

    data class HasOnlyFixtures(
        override val isLoading: Boolean,
        override val error: HomeError,
        override val searchQuery: String,
        val leagueFixturesDataList: List<LeagueFixturesData>
    ) : HomeUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: HomeError,
        override val searchQuery: String
    ) : HomeUiState
}