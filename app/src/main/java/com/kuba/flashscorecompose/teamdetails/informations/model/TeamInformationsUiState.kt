package com.kuba.flashscorecompose.teamdetails.informations.model

import com.kuba.flashscorecompose.data.team.information.model.Coach
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
        val teamCountry: TeamCountry,
        val venue: Venue,
        val coach: Coach
    ) : TeamInformationsUiState

    data class HasDataWithoutCoach(
        override val isLoading: Boolean,
        override val error: TeamInformationsError,
        val teamCountry: TeamCountry,
        val venue: Venue
    ) : TeamInformationsUiState

    data class HasDataWithoutVenueAndCoach(
        override val isLoading: Boolean,
        override val error: TeamInformationsError,
        val teamCountry: TeamCountry
    ) : TeamInformationsUiState

    data class NoData(
        override val isLoading: Boolean,
        override val error: TeamInformationsError
    ) : TeamInformationsUiState
}