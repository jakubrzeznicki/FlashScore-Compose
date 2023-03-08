package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class GoalsDto(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)
