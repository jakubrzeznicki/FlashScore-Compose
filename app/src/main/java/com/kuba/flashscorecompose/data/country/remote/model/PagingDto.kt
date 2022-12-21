package com.kuba.flashscorecompose.data.country.remote.model

import com.google.gson.annotations.SerializedName

data class PagingDto(
    @SerializedName("current") val current: Int,
    @SerializedName("total") val total: Int
)