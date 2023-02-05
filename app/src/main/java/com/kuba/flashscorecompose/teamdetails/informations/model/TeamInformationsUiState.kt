package com.kuba.flashscorecompose.teamdetails.informations.model

import com.kuba.flashscorecompose.data.country.model.Country
import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Venue

/**
 * Created by jrzeznicki on 27/01/2023.
 */
interface TeamInformationsUiState {
    val isLoading: Boolean
    val error: TeamInformationsError

    data class HasFullData(
        override val isLoading: Boolean,
        override val error: TeamInformationsError,
        val team: Team,
        val country: Country,
        val venue: Venue,
        val coach: Coach
    ) : TeamInformationsUiState

    data class HasDataWithoutCoach(
        override val isLoading: Boolean,
        override val error: TeamInformationsError,
        val country: Country,
        val team: Team,
        val venue: Venue
    ) : TeamInformationsUiState

    data class HasDataWithoutVenueAndCoach(
        override val isLoading: Boolean,
        override val error: TeamInformationsError,
        val team: Team,
        val country: Country
        ) : TeamInformationsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: TeamInformationsError
    ) : TeamInformationsUiState
}