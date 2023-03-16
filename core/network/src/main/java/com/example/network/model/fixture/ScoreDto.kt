package com.example.network.model.fixture

import com.google.gson.annotations.SerializedName

data class ScoreDto(
    @SerializedName("extratime") val extratime: GoalsDto?,
    @SerializedName("fulltime") val fulltime: GoalsDto?,
    @SerializedName("halftime") val halftime: GoalsDto?,
    @SerializedName("penalty") val penalty: GoalsDto?
)
