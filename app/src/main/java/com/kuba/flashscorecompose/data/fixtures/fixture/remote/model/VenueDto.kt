package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class VenueDto(
    @SerializedName("city") val city: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
)