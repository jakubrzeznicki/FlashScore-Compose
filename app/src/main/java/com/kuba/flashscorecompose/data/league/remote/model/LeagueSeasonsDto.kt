package com.kuba.flashscorecompose.data.league.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.country.remote.model.CountryDto

data class LeagueSeasonsDto(
    @SerializedName("league") val league: LeagueDto?,
    @SerializedName("country") val country: CountryDto?,
    @SerializedName("seasons") val seasons: List<SeasonDto>?
)