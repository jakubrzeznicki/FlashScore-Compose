package com.kuba.flashscorecompose.teamdetails.informations.viewmodel

import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Venue
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamCountry
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsError
import com.kuba.flashscorecompose.teamdetails.informations.model.TeamInformationsUiState

/**
 * Created by jrzeznicki on 27/01/2023.
 */
data class TeamInformationsViewModelState(
    val isLoading: Boolean = false,
    val error: TeamInformationsError = TeamInformationsError.NoError,
    val teamCountry: TeamCountry? = TeamCountry.EMPTY_TEAM_COUNTRY,
    val venue: Venue? = Venue.EMPTY_VENUE,
    val coach: Coach? = Coach.EMPTY_COACH,
) {
    fun toUiState(): TeamInformationsUiState = when {
        teamCountry != null && venue != null && coach != null ->
            TeamInformationsUiState.HasFullData(isLoading, error, teamCountry, venue, coach)
        teamCountry != null && venue != null && coach == null ->
            TeamInformationsUiState.HasDataWithoutCoach(isLoading, error, teamCountry, venue)
        teamCountry != null && venue == null && coach == null ->
            TeamInformationsUiState.HasDataWithoutVenueAndCoach(isLoading, error, teamCountry)
        else -> TeamInformationsUiState.NoData(isLoading, error)
    }
}