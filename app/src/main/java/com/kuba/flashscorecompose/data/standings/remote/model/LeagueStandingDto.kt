package com.kuba.flashscorecompose.data.standings.remote.model

import com.google.gson.annotations.SerializedName

data class LeagueStandingDto(
    @SerializedName("league") val league: StandingsDto?
)