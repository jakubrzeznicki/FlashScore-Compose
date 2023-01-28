package com.kuba.flashscorecompose.data.team.information.remote.model

import com.google.gson.annotations.SerializedName

data class VenueDto(
    @SerializedName("city") val city: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("capacity") val capacity: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("surface") val surface: String?,
    @SerializedName("image") val image: String?
)