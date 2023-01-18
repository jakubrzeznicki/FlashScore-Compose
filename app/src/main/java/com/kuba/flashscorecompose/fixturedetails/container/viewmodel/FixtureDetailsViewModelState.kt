package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsUiState

/**
 * Created by jrzeznicki on 10/01/2023.
 */
data class FixtureDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: FixtureDetailsError = FixtureDetailsError.NoError,
    val fixtureItem: FixtureItem? = null
) {
    fun toUiState(): FixtureDetailsUiState = if (fixtureItem == null) {
        FixtureDetailsUiState.NoData(isLoading, error)
    } else {
        FixtureDetailsUiState.HasData(isLoading, error, fixtureItem)
    }
}