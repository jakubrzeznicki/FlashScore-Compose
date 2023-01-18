package com.kuba.flashscorecompose.data.league.remote.model

import com.google.gson.annotations.SerializedName

data class SeasonDto(
    @SerializedName("current") val current: Boolean?,
    @SerializedName("end") val end: String?,
    @SerializedName("start") val start: String?,
    @SerializedName("year") val year: Int?
)