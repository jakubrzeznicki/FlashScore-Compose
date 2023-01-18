package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName

data class SubstituteDto(
    @SerializedName("player") val player: PlayerDto
)