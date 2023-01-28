package com.kuba.flashscorecompose.teamdetails.container.viewmodel

import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsUiState

/**
 * Created by jrzeznicki on 26/01/2023.
 */
data class TeamDetailsViewModelState(
    val team: Team = Team.EMPTY_TEAM,
    val leagueId: Int = 0
) {
    fun toUiState(): TeamDetailsUiState =
        TeamDetailsUiState(team = team, leagueId = leagueId)
}