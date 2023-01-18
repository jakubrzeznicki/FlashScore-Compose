package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class PeriodsDto(
    @SerializedName("first") val first: Int?,
    @SerializedName("second") val second: Int?,
)