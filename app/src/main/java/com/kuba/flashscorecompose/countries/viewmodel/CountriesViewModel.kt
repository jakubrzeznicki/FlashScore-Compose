package com.kuba.flashscorecompose.countries.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscorecompose.countries.model.CountriesError
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by jrzeznicki on 9/9/2022
 */
class CountriesViewModel(private val countryRepository: CountryDataSource) : ViewModel() {

    private val viewModelState = MutableStateFlow(CountriesViewModelState())

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

    fun setup() {
        refreshCountries()
        observeCountries()
    }

    fun observeCountries() {
        viewModelScope.launch {
            countryRepository.observeCountries().collect() { countries ->
                viewModelState.update { it.copy(countryItems = countries) }
            }
        }
    }

    fun refreshCountries() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = countryRepository.loadCountries()
            viewModelState.update {
                when (result) {
                    is RepositoryResult.Success -> it.copy(isLoading = false)
                    is RepositoryResult.Error ->
                        it.copy(isLoading = false, error = CountriesError.RemoteError(result.error))
                }
            }
        }
    }

    fun cleanError() {
        viewModelState.update { it.copy(error = CountriesError.NoError) }
    }
}