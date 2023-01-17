package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class PlayerColorDto(
    @SerializedName("border") val border: String?,
    @SerializedName("number") val number: String?,
    @SerializedName("primary") val primary: String?
)