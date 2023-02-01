package com.kuba.flashscorecompose.standings.viewmodel

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.standings.model.StandingsUiState

/**
 * Created by jrzeznicki on 18/01/2023.
 */
data class StandingsViewModelState(
    val isLoading: Boolean = false,
    val error: StandingsError = StandingsError.NoError,
    val standingsQuery: String = "",
    val standings: List<Standing> = emptyList(),
    val filteredStandings: List<Standing> = emptyList(),
    val selectedCountry: Country = Country.EMPTY_COUNTRY,
    val countries: List<Country> = emptyList()
) {
    fun toUiState(): StandingsUiState = when {
        filteredStandings.isNotEmpty() && countries.isNotEmpty() ->
            StandingsUiState.HasAllData(
                isLoading,
                error,
                standingsQuery,
                filteredStandings,
                selectedCountry,
                countries
            )
        countries.isNotEmpty() ->
            StandingsUiState.HasOnlyCountries(
                isLoading,
                error,
                standingsQuery,
                selectedCountry,
                countries
            )
        else -> StandingsUiState.NoData(isLoading, error, standingsQuery)

    }
}