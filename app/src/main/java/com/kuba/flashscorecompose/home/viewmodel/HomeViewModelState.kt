package com.kuba.flashscorecompose.home.viewmodel

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.HomeUiState
import com.kuba.flashscorecompose.home.model.LeagueFixturesData

/**
 * Created by jrzeznicki on 05/01/2023.
 */
data class HomeViewModelState(
    val isLoading: Boolean = false,
    val error: HomeError = HomeError.NoError,
    val leagueFixturesDataList: List<LeagueFixturesData> = emptyList(),
    val filteredLeagueFixtureDataList: List<LeagueFixturesData> = emptyList(),
    val selectedCountry: Country = Country.EMPTY_COUNTRY,
    val searchQuery: String = "",
    val isSearchExpanded: Boolean = false,
    val countries: List<Country> = emptyList(),
    val date: String = ""
) {
    fun toUiState(): HomeUiState = when {
        filteredLeagueFixtureDataList.isNotEmpty() && countries.isNotEmpty() ->
            HomeUiState.HasAllData(
                isLoading,
                error,
                searchQuery,
                isSearchExpanded,
                countries,
                selectedCountry,
                filteredLeagueFixtureDataList
            )
        countries.isNotEmpty() ->
            HomeUiState.HasOnlyCountries(
                isLoading,
                error,
                searchQuery,
                isSearchExpanded,
                selectedCountry,
                countries
            )
        else -> HomeUiState.NoData(isLoading, error, searchQuery, isSearchExpanded)
    }
}
