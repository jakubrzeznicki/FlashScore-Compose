package com.example.network.model.standing

import com.google.gson.annotations.SerializedName

data class StandingsDataDto(
    @SerializedName("response") val response: List<LeagueStandingDto>?
)
