package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import android.util.Log
import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetaislUiState

/**
 * Created by jrzeznicki on 10/01/2023.
 */
data class FixtureDetailsViewModelState(
    val isLoading: Boolean = false,
    val error: FixtureDetailsError = FixtureDetailsError.NoError,
    val fixtureItem: FixtureItem? = null
) {
    fun toUiState(): FixtureDetaislUiState = if (fixtureItem == null) {
        Log.d("TEST_LOG", "UiState no data - fixtureItem - ${fixtureItem}")
        FixtureDetaislUiState.NoData(isLoading, error)
    } else {
        Log.d("TEST_LOG", "UiState else  - fixtureItem - ${fixtureItem}")
        FixtureDetaislUiState.HasData(isLoading, error, fixtureItem)
    }
}