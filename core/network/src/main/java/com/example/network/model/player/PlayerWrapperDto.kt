package com.example.network.model.player

import com.google.gson.annotations.SerializedName

data class PlayerWrapperDto(@SerializedName("player") val player: PlayerDto?)
