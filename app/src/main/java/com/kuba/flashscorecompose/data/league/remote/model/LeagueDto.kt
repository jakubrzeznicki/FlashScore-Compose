package com.kuba.flashscorecompose.data.league.remote.model

import com.google.gson.annotations.SerializedName

data class LeagueDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("country") val countryName: String?,
    @SerializedName("flag") val flag: String?,
    @SerializedName("season") val season: Int?,
    @SerializedName("round") val round: String?
)