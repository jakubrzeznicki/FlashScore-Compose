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
    val countryItems: List<Country> = emptyList(),
) {
    fun toUiState(): HomeUiState = if (leagueFixturesDataList.isEmpty() && countryItems.isEmpty()) {
        HomeUiState.NoData(isLoading, error)
    } else {
        HomeUiState.HasData(isLoading, error, countryItems, leagueFixturesDataList)
    }
}