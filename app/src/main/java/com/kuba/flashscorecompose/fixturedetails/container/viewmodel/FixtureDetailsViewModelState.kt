package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem.Companion.EMPTY_FIXTURE_ITEM
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsUiState

/**
 * Created by jrzeznicki on 07/03/2023.
 */
data class FixtureDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: FixtureDetailsError = FixtureDetailsError.NoError,
    val fixtureItem: FixtureItem? = EMPTY_FIXTURE_ITEM
) {
    fun toUiState(): FixtureDetailsUiState = if (fixtureItem != null) {
        FixtureDetailsUiState.HasData(isLoading, error, fixtureItem)
    } else {
        FixtureDetailsUiState.NoData(isLoading, error)
    }
}