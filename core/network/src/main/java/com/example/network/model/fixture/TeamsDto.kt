package com.example.network.model.fixture

import com.example.network.model.team.TeamDto
import com.google.gson.annotations.SerializedName

data class TeamsDto(
    @SerializedName("away") val away: TeamDto?,
    @SerializedName("home") val home: TeamDto?
)
