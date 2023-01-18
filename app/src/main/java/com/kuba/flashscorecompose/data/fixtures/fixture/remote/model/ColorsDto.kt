package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class ColorsDto(
    @SerializedName("goalkeeper") val goalkeeper: PlayerColorDto?,
    @SerializedName("player") val player: PlayerColorDto?
)