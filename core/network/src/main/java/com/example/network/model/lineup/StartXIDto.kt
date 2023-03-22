package com.example.network.model.lineup

import com.example.network.model.player.PlayerDto
import com.google.gson.annotations.SerializedName

data class StartXIDto(
    @SerializedName("player") val player: PlayerDto?
)
