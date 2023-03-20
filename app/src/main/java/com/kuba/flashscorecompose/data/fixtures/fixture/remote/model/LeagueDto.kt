package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 20/02/2023.
 */
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
