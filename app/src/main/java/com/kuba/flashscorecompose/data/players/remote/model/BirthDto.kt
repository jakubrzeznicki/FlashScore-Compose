package com.kuba.flashscorecompose.data.players.remote.model

import com.google.gson.annotations.SerializedName

data class BirthDto(
    @SerializedName("date") val date: String?,
    @SerializedName("place") val place: String?,
    @SerializedName("country") val country: String?
)
