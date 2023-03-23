package com.example.network.model.statistics

import com.google.gson.annotations.SerializedName

data class StatisticDto(
    @SerializedName("type") val type: String?,
    @SerializedName("value") val value: Any?
)
