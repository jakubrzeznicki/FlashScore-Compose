package com.example.network.model.country

import com.google.gson.annotations.SerializedName

data class PagingDto(
    @SerializedName("current") val current: Int,
    @SerializedName("total") val total: Int
)
