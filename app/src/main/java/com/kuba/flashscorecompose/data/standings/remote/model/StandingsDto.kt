package com.kuba.flashscorecompose.data.standings.remote.model

import com.google.gson.annotations.SerializedName

data class StandingsDto(
    @SerializedName("country") val country: String?,
    @SerializedName("flag") val flag: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("season") val season: Int?,
    @SerializedName("standings") val standings: List<List<StandingItemDto>?>
)
