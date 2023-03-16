package com.example.teamdetails.informations.viewmodel

import com.example.model.country.Country
import com.example.model.team.Coach
import com.example.model.team.Team
import com.example.model.team.Venue
import com.example.teamdetails.informations.model.TeamInformationsError
import com.example.teamdetails.informations.model.TeamInformationsUiState

/**
 * Created by jrzeznicki on 27/01/2023.
 */
data class TeamInformationsViewModelState(
    val isLoading: Boolean = false,
    val error: TeamInformationsError = TeamInformationsError.NoError,
    val team: Team? = Team.EMPTY_TEAM,
    val country: Country = Country.EMPTY_COUNTRY,
    val venue: Venue? = Venue.EMPTY_VENUE,
    val coach: Coach? = Coach.EMPTY_COACH
) {
    fun toUiState(): TeamInformationsUiState = when {
        team != null && venue != null && coach != null ->
            TeamInformationsUiState.HasFullData(isLoading, error, team, country, venue, coach)
        team != null && venue != null && coach == null ->
            TeamInformationsUiState.HasDataWithoutCoach(isLoading, error, country, team, venue)
        team != null && venue == null && coach == null ->
            TeamInformationsUiState.HasDataWithoutVenueAndCoach(isLoading, error, team, country)
        else -> TeamInformationsUiState.NoData(isLoading, error)
    }
}
