package com.kuba.flashscorecompose.home.viewmodel

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.home.model.HomeError
import com.kuba.flashscorecompose.home.model.HomeUiState

/**
 * Created by jrzeznicki on 05/01/2023.
 */
data class HomeViewModelState(
    val isLoading: Boolean = false,
    val error: HomeError = HomeError.NoError,
    val fixtureItems: List<FixtureItem> = emptyList(),
    val countryItems: List<Country> = emptyList(),
) {
    fun toUiState(): HomeUiState = if (fixtureItems.isEmpty() && countryItems.isEmpty()) {
        HomeUiState.NoData(isLoading, error)
    } else {
        HomeUiState.HasData(isLoading, error, countryItems, fixtureItems)
    }
}