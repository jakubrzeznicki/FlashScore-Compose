package com.kuba.flashscorecompose.home.model

import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 05/01/2023.
 */
sealed interface HomeUiState {
    val isLoading: Boolean
    val error: HomeError

    data class HasData(
        override val isLoading: Boolean,
        override val error: HomeError,
        val countryItems: List<Country>,
        val leagueFixturesDataList: List<LeagueFixturesData>
    ) : HomeUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: HomeError
    ) : HomeUiState
}