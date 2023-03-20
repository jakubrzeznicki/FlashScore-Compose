package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.team.information.remote.model.VenueDto

data class FixtureInfoDto(
    @SerializedName("date") val date: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("referee") val referee: String?,
    @SerializedName("status") val status: StatusDto?,
    @SerializedName("timestamp") val timestamp: Long?,
    @SerializedName("timezone") val timezone: String?,
    @SerializedName("venue") val venue: VenueDto?,
    @SerializedName("periods") val periods: PeriodsDto?
)
