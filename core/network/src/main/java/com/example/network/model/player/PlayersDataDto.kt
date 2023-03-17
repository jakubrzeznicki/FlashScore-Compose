package com.example.network.model.player

import com.google.gson.annotations.SerializedName

/**
 * Created by jrzeznicki on 28/01/2023.
 */
data class PlayersDataDto(
    @SerializedName("response") val response: List<PlayerWrapperDto>
)