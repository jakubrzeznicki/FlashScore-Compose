package com.example.network.model.fixture

import com.example.network.model.team.VenueDto
import com.google.gson.annotations.SerializedName

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
