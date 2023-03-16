package com.example.network.model.standing

import com.google.gson.annotations.SerializedName

data class LeagueStandingDto(
    @SerializedName("league") val league: StandingsDto?
)
