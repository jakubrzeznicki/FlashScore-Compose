package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDto

data class TeamsDto(
    @SerializedName("away") val away: TeamDto?,
    @SerializedName("home") val home: TeamDto?
)