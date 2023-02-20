package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.players.remote.model.PlayerDto

data class SubstituteDto(
    @SerializedName("player") val player: PlayerDto?
)