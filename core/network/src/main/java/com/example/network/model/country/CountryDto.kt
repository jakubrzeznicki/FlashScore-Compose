package com.example.network.model.country

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 20/12/2022.
 */
data class CountryDto(
    @SerializedName("code") val code: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("flag") val flag: String?
)
