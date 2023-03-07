package com.kuba.flashscorecompose.fixturedetails.container.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem

/**
 * Created by jrzeznicki on 07/03/2023.
 */
sealed interface FixtureDetailsUiState {
    val isLoading: Boolean
    val error: FixtureDetailsError

    data class HasData(
        override val isLoading: Boolean,
        override val error: FixtureDetailsError,
        val fixtureItem: FixtureItem
    ) : FixtureDetailsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: FixtureDetailsError
    ) : FixtureDetailsUiState
}