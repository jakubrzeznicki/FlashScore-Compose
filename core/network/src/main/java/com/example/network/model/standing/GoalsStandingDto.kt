package com.example.network.model.standing

import com.google.gson.annotations.SerializedName

data class GoalsStandingDto(
    @SerializedName("against") val against: Int?,
    @SerializedName("for")val forValue: Int?
)
