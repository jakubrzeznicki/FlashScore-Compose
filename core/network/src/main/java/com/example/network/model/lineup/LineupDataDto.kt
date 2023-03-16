package com.example.network.model.lineup

import com.google.gson.annotations.SerializedName

data class LineupDataDto(
    @SerializedName("response") val response: List<LineupDto>
)
