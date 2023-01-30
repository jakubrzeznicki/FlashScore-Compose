package com.kuba.flashscorecompose.standingsdetails.model

import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.standings.model.StandingItem
import com.kuba.flashscorecompose.ui.component.chips.FilterChip

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class StandingsDetailsUiState(
    val league: League,
    val standingFilterChip: FilterChip.Standings,
    val standingsItems: List<StandingItem>
)