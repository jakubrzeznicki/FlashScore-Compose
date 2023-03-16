package com.example.network.model.fixture

import com.google.gson.annotations.SerializedName

data class GoalsDto(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)
