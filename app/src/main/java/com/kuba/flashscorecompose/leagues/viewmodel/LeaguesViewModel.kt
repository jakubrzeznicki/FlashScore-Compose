package com.kuba.flashscorecompose.leagues.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.leagues.model.LeaguesError
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 10/3/2022
 */
class LeaguesViewModel(
    private val countryId: String,
    private val leagueRepository: LeagueDataSource
) : ViewModel() {

    private val viewModelState = MutableStateFlow(LeaguesViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        refreshLeagues()
        observeLeagues()
    }

    private fun observeLeagues() {
        viewModelScope.launch {
            leagueRepository.observeLeagues(countryId).collect { leagues ->
                viewModelState.update { it.copy(leagueItems = leagues) }
            }
        }
    }

    fun refreshLeagues() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = leagueRepository.loadLeagues(countryId)
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error ->
                        it.copy(isLoading = false, error = LeaguesError.RemoteError(result.error))
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = LeaguesError.NoError) }
    }
}