package com.kuba.flashscorecompose.teamdetails.container.viewmodel

import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsError
import com.kuba.flashscorecompose.teamdetails.container.model.TeamDetailsUiState

/**
 * Created by jrzeznicki on 26/01/2023.
 */
data class TeamDetailsViewModelState(
    val error: TeamDetailsError = TeamDetailsError.NoError,
    val team: Team? = Team.EMPTY_TEAM,
) {
    fun toUiState(): TeamDetailsUiState = if (team != null && team != Team.EMPTY_TEAM) {
        TeamDetailsUiState.HasData(error, team)
    } else {
        TeamDetailsUiState.NoData(error)
    }
}