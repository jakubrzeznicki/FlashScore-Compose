package com.kuba.flashscorecompose.teamdetails.container.model

/**
 * Created by jrzeznicki on 01/02/2023.
 */
sealed class TeamDetailsError {
    object NoError : TeamDetailsError()
    object EmptyTeamDetails : TeamDetailsError()
}