package com.example.standings.model

import com.example.model.country.Country
import com.example.model.standings.Standing

/**
 * Created by jrzeznicki on 18/01/2023.
 */
sealed interface StandingsUiState {
    val isLoading: Boolean
    val error: StandingsError
    val standingsQuery: String

    data class HasAllData(
        override val isLoading: Boolean,
        override val error: StandingsError,
        override val standingsQuery: String,
        val standings: List<Standing>,
        val selectedCountry: Country,
        val countries: List<Country>
    ) : StandingsUiState

    data class HasOnlyCountries(
        override val isLoading: Boolean,
        override val error: StandingsError,
        override val standingsQuery: String,
        val selectedCountry: Country,
        val countries: List<Country>
    ) : StandingsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StandingsError,
        override val standingsQuery: String
    ) : StandingsUiState
}
