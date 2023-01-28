package com.kuba.flashscorecompose.teamdetails.container.model

import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 26/01/2023.
 */
data class TeamDetailsUiState(
    val team: Team?,
    val leagueId: Int
)