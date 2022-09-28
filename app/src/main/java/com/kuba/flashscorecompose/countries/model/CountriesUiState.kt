package com.kuba.flashscorecompose.countries.model

import com.kuba.flashscorecompose.data.country.model.Country

/**
 * Created by jrzeznicki on 9/9/2022
 */
sealed interface CountriesUiState {
    val isLoading: Boolean
    val error: CountriesError

    data class NoCountries(override val isLoading: Boolean, override val error: CountriesError) :
        CountriesUiState

    data class HasCountries(
        override val isLoading: Boolean,
        override val error: CountriesError,
        val countryItems: List<Country>
    ) : CountriesUiState
}