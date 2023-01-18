package com.kuba.flashscorecompose.leagues.model

import com.kuba.flashscorecompose.data.league.model.League

/**
 * Created by jrzeznicki on 10/1/2022
 */
sealed interface LeaguesUiState {
    val isLoading: Boolean
    val error: LeaguesError
    val dayItems: List<DayItem>

    data class NoLeagues(
        override val isLoading: Boolean,
        override val error: LeaguesError,
        override val dayItems: List<DayItem>
    ) : LeaguesUiState

    data class HasLeagues(
        override val isLoading: Boolean,
        override val error: LeaguesError,
        override val dayItems: List<DayItem>,
        val leagueItems: List<League>,
        val favoriteLeagueItems: List<League>
    ) : LeaguesUiState
}