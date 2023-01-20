package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class StandingsDetailsUiState(
    val league: League,
    val standingButtonState: FilteredButton = FilteredButton.All,
    val standingsItems: List<StandingItem> = emptyList()
)