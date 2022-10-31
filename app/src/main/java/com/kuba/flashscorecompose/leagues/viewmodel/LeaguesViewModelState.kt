package com.kuba.flashscorecompose.leagues.viewmodel

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leagues.model.LeaguesError
import com.kuba.flashscorecompose.leagues.model.LeaguesUiState

/**
 * Created by jrzeznicki on 10/3/2022
 */
data class LeaguesViewModelState(
    val isLoading: Boolean = false,
    val error: LeaguesError = LeaguesError.NoError,
    val leagueItems: List<League> = emptyList()
) {
    fun toUiState(): LeaguesUiState = if (leagueItems.isEmpty()) {
        LeaguesUiState.NoLeagues(isLoading, error)
    } else {
        LeaguesUiState.HasLeagues(isLoading, error, leagueItems)
    }
}