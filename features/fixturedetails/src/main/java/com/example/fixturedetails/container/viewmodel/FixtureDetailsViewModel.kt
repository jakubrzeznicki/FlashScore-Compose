package com.example.fixturedetails.container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.utils.RepositoryResult
import com.example.data.fixture.repository.FixturesDataSource
import com.example.fixturedetails.container.model.FixtureDetailsError
import com.example.ui.snackbar.SnackbarManager
import com.example.ui.snackbar.SnackbarMessageType
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
        //refreshFixtureItem()
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
                            error = FixtureDetailsError.RemoteError(result.error)
                        )
                    }
                }
            }
        }
    }
}
