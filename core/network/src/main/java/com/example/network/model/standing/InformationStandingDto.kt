package com.example.network.model.standing

import com.google.gson.annotations.SerializedName

data class InformationStandingDto(
    @SerializedName("draw") val draw: Int?,
    @SerializedName("goals") val goals: GoalsStandingDto?,
    @SerializedName("lose") val lose: Int?,
    @SerializedName("played") val played: Int?,
    @SerializedName("win") val win: Int?
)
