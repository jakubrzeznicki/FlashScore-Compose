package com.kuba.flashscorecompose.leagues.model

import com.kuba.flashscorecompose.data.league.model.League

/**
 * Created by jrzeznicki on 10/1/2022
 */
sealed interface LeaguesUiState {
    val isLoading: Boolean
    val error: LeaguesError

    data class NoLeagues(override val isLoading: Boolean, override val error: LeaguesError) :
        LeaguesUiState

    data class HasLeagues(
        override val isLoading: Boolean,
        override val error: LeaguesError,
        val countryItems: List<League>
    ) : LeaguesUiState
}