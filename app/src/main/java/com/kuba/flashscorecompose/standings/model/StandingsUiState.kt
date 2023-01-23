package com.kuba.flashscorecompose.standings.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.standings.model.Standing

/**
 * Created by jrzeznicki on 18/01/2023.
 */
sealed interface StandingsUiState {
    val isLoading: Boolean
    val error: StandingsError
    val standingsQuery: String

    data class HasData(
        override val isLoading: Boolean,
        override val error: StandingsError,
        override val standingsQuery: String,
        val standings: List<Standing>,
        val selectedCountry: Country,
        val countries: List<Country>
    ) : StandingsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: StandingsError,
        override val standingsQuery: String
    ) : StandingsUiState
}