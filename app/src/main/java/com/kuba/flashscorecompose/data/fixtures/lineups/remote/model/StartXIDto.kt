package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName

data class StartXIDto(
    @SerializedName("player") val player: PlayerDto
)