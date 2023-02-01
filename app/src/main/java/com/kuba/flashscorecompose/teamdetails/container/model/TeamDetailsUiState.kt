package com.kuba.flashscorecompose.teamdetails.container.model

import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 26/01/2023.
 */
interface TeamDetailsUiState {
    val error: TeamDetailsError

    data class HasData(
        override val error: TeamDetailsError,
        val team: Team
    ) : TeamDetailsUiState

    data class NoData(override val error: TeamDetailsError) : TeamDetailsUiState
}