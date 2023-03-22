package com.example.network.model.fixture

import com.google.gson.annotations.SerializedName

data class PeriodsDto(
    @SerializedName("first") val first: Int?,
    @SerializedName("second") val second: Int?
)
