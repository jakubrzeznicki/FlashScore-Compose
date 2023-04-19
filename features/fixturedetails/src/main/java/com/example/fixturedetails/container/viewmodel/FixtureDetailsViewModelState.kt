package com.example.fixturedetails.container.viewmodel

import com.example.fixturedetails.container.model.FixtureDetailsError
import com.example.fixturedetails.container.model.FixtureDetailsUiState
import com.example.model.fixture.FixtureItem
import com.example.model.fixture.FixtureItem.Companion.EMPTY_FIXTURE_ITEM

/**
 * Created by jrzeznicki on 07/03/2023.
 */
data class FixtureDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: FixtureDetailsError = FixtureDetailsError.NoError,
    val fixtureItem: FixtureItem? = EMPTY_FIXTURE_ITEM
) {
    fun toUiState(): FixtureDetailsUiState = if (fixtureItem!= null && fixtureItem.id != 0) {
        FixtureDetailsUiState.HasData(isLoading, error, fixtureItem)
    } else {
        FixtureDetailsUiState.NoData(isLoading, error)
    }
}
