package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 10/01/2023.
 */
class FixtureDetailsViewModel(
    private val fixtureId: Int,
    private val fixturesRepository: FixturesDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(FixtureDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        getFixtureItem()
    }

    private fun getFixtureItem() {
        viewModelScope.launch {
            val fixtureItem = fixturesRepository.getFixture(fixtureId)
            viewModelState.update {
                it.copy(fixtureItem = fixtureItem)
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = FixtureDetailsError.NoError) }
    }
}