package com.kuba.flashscorecompose.countries.viewmodel

import com.kuba.flashscorecompose.countries.model.CountriesError
import com.kuba.flashscorecompose.countries.model.CountriesUiState
import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 9/9/2022
 */
data class CountriesViewModelState(
    val isLoading: Boolean = false,
    val error: CountriesError = CountriesError.NoError,
    val countryItems: List<Country> = emptyList()
) {
    fun toUiState(): CountriesUiState = if (countryItems.isEmpty()) {
        CountriesUiState.NoCountries(isLoading, error)
    } else {
        CountriesUiState.HasCountries(isLoading, error, countryItems)
    }
}