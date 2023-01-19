package com.kuba.flashscorecompose.data.standings.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.TeamDto

data class StandingItemDto(
    @SerializedName("all") val all: InformationStandingDto?,
    @SerializedName("away") val away: InformationStandingDto?,
    @SerializedName("home") val home: InformationStandingDto?,
    @SerializedName("description") val description: String?,
    @SerializedName("form") val form: String?,
    @SerializedName("goalsDiff") val goalsDiff: Int?,
    @SerializedName("group") val group: String?,
    @SerializedName("points") val points: Int?,
    @SerializedName("rank") val rank: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("team") val team: TeamDto?,
    @SerializedName("update") val update: String?
)