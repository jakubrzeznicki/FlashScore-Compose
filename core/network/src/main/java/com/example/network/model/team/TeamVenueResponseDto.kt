package com.example.network.model.team

import com.google.gson.annotations.SerializedName

data class TeamVenueResponseDto(
    @SerializedName("team") val team: TeamDto,
    @SerializedName("venue") val venue: VenueDto
)
