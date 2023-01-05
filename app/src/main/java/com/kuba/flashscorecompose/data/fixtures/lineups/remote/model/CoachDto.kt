package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName

data class CoachDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?
)