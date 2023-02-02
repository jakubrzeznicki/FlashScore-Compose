package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.utils.format
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

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
                if (fixtureItem != null) {
                    val dateTime = Instant.ofEpochSecond(fixtureItem.fixture.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                    val formattedDate = dateTime.format(PATTERN)
                    val updatedFixtureInfo = fixtureItem.fixture.copy(date = formattedDate)
                    it.copy(fixtureItem = fixtureItem.copy(fixture = updatedFixtureInfo))
                } else {
                    it.copy(error = FixtureDetailsError.EmptyDatabase)
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = FixtureDetailsError.NoError) }
    }

    private companion object {
        const val PATTERN = "dd.MM.yyyy HH:mm"
    }
}