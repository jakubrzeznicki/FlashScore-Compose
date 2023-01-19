package com.kuba.flashscorecompose.standings.viewmodel

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.standings.model.Standings
import com.kuba.flashscorecompose.standings.model.StandingsError
import com.kuba.flashscorecompose.standings.model.StandingsUiState

/**
 * Created by jrzeznicki on 18/01/2023.
 */
data class StandingsViewModelState(
    val isLoading: Boolean = false,
    val error: StandingsError = StandingsError.NoError,
    val standingsQuery: String = "",
    val standings: List<Standings> = emptyList(),
    val filteredStandings: List<Standings> = emptyList(),
    val countryItems: List<Country> = emptyList()
) {
    fun toUiState(): StandingsUiState = if (standings.isNotEmpty() && countryItems.isNotEmpty()) {
        StandingsUiState.HasData(
            isLoading,
            error,
            standingsQuery,
            standings,
            filteredStandings,
            countryItems
        )
    } else {
        StandingsUiState.NoData(isLoading, error, standingsQuery)
    }
}