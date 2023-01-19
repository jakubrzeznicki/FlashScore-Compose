package com.kuba.flashscorecompose.data.standings.remote.model

import com.google.gson.annotations.SerializedName

data class GoalsStandingDto(
    @SerializedName("against") val against: Int?,
    @SerializedName("for")val forValue: Int?
)