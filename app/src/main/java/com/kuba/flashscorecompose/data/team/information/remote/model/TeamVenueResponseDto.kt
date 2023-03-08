package com.kuba.flashscorecompose.data.team.information.remote.model

import com.google.gson.annotations.SerializedName

data class TeamVenueResponseDto(
    @SerializedName("team") val team: TeamDto,
    @SerializedName("venue") val venue: VenueDto
)
