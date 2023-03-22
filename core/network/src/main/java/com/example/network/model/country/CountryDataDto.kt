package com.example.network.model.country

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 20/12/2022.
 */
data class CountryDataDto(
    @SerializedName("errors") val errors: List<Any>?,
    @SerializedName("paging") val paging: PagingDto?,
    @SerializedName("response") val countries: List<CountryDto>?
)
