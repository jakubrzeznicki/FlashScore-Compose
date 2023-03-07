package com.kuba.flashscorecompose.fixturedetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.fixtures.fixture.FixturesDataSource
import com.kuba.flashscorecompose.fixturedetails.container.model.FixtureDetailsError
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarManager
import com.kuba.flashscorecompose.ui.component.snackbar.SnackbarMessageType
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 07/03/2023.
 */
class FixtureDetailsViewModel(
    private val fixtureItemId: Int,
    private val fixturesRepository: FixturesDataSource,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val viewModelState = MutableStateFlow(FixtureDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            viewModelState.value.toUiState()
        )

    fun setup() {
        observeFixtureItem()
    }

    private fun observeFixtureItem() {
        viewModelScope.launch {
            fixturesRepository.observeFixtureById(fixtureItemId).collect { fixtureItem ->
                if (fixtureItem == null) {
                    viewModelState.update { it.copy(error = FixtureDetailsError.EmptyFixtureItem) }
                    return@collect
                }
                viewModelState.update { it.copy(fixtureItem = fixtureItem) }
            }
        }
    }

    fun refreshFixtureItem() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fixturesRepository.loadFixtureById(fixtureItemId)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error -> {
                        snackbarManager.showSnackbarMessage(
                            result.error.statusMessage,
                            SnackbarMessageType.Error
                        )
                        it.copy(
                            isLoading = false,
                            error = FixtureDetailsError.RemoteError(result.error),
                        )
                    }
                }
            }
        }
    }
}