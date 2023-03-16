package com.example.fixturedetails.container.model

import com.example.model.fixture.FixtureItem

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
