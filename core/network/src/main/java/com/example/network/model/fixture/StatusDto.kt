package com.example.network.model.fixture

import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("elapsed") val elapsed: Int?,
    @SerializedName("long") val long: String?,
    @SerializedName("short") val short: String?
)
