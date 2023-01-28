package com.kuba.flashscorecompose.teamdetails.informations.viewmodel

import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsError
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsUiState

/**
 * Created by jrzeznicki on 27/01/2023.
 */
data class TeamInformationsViewModelState(
    val isLoading: Boolean = false,
    val error: TeamInformationsError = TeamInformationsError.NoError,
    val team: Team? = Team.EMPTY_TEAM,
    val venue: Venue? = Venue.EMPTY_VENUE,
    val coach: Coach? = Coach.EMPTY_COACH,
) {
    fun toUiState(): TeamInformationsUiState = when {
        team != null && venue != null && coach != null ->
            TeamInformationsUiState.HasFullData(isLoading, error, team, venue, coach)
        team != null && venue != null && coach == null ->
            TeamInformationsUiState.HasDataWithoutCoach(isLoading, error, team, venue)
        team != null && venue == null && coach == null ->
            TeamInformationsUiState.HasDataWithoutVenueAndCoach(isLoading, error, team)
        else -> TeamInformationsUiState.NoData(isLoading, error)
    }
}