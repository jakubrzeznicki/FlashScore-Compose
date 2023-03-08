package com.kuba.flashscorecompose.data.fixtures.statistics.remote.model

import com.google.gson.annotations.SerializedName

data class StatisticDto(
    @SerializedName("type") val type: String?,
    @SerializedName("value") val value: Any?
)
