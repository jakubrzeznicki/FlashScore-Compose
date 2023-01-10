package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class FixtureInfoDto(
    @SerializedName("date") val date: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("referee") val referee: String?,
    @SerializedName("status") val status: StatusDto?,
    @SerializedName("timestamp") val timestamp: Int?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("venue") val venue: VenueDto?,
    @SerializedName("periods") val periods: PeriodsDto?
)