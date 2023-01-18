package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class TeamsDto(
    @SerializedName("away") val away: TeamDto?,
    @SerializedName("home") val home: TeamDto?
)