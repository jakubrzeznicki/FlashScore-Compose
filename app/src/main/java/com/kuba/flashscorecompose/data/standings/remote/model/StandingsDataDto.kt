package com.kuba.flashscorecompose.data.standings.remote.model

import com.google.gson.annotations.SerializedName

data class StandingsDataDto(
    @SerializedName("response") val response: List<LeagueStandingDto>?
)