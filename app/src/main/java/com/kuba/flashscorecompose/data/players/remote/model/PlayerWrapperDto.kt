package com.kuba.flashscorecompose.data.players.remote.model

import com.google.gson.annotations.SerializedName

data class PlayerWrapperDto(@SerializedName("player") val player: PlayerDto)