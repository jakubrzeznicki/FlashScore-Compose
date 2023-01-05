package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName

data class PlayerDto(
    @SerializedName("grid") val grid: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("number") val number: Int?,
    @SerializedName("pos") val pos: String?
)