package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName

data class LineupDataDto(
    @SerializedName("response") val response: List<LineupDto>
)