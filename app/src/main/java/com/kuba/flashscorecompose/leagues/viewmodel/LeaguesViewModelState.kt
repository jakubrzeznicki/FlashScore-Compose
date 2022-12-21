package com.kuba.flashscorecompose.leagues.viewmodel

import android.util.Log
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.leagues.model.DayItem
import com.kuba.flashscorecompose.leagues.model.LeaguesError
import com.kuba.flashscorecompose.leagues.model.LeaguesUiState

/**
 * Created by jrzeznicki on 10/3/2022
 */
data class LeaguesViewModelState(
    val isLoading: Boolean = false,
    val error: LeaguesError = LeaguesError.NoError,
    val leagueItems: List<League> = emptyList(),
    val favoriteItems: List<League> = emptyList(),
    val dayItems: List<DayItem> = emptyList()
) {
    fun toUiState(): LeaguesUiState = if (leagueItems.isEmpty()) {
        Log.d("TEST_LOG", "leagueItems are empty")
        LeaguesUiState.NoLeagues(isLoading, error, dayItems)
    } else {
        Log.d("TEST_LOG", "leagueItems has ${leagueItems.size} items")
        LeaguesUiState.HasLeagues(isLoading, error, dayItems, leagueItems, favoriteItems)
    }
}