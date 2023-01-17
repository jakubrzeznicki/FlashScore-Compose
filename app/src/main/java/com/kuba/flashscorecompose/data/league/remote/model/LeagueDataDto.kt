package com.kuba.flashscorecompose.data.league.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.country.remote.model.PagingDto

data class LeagueDataDto(
    @SerializedName("paging") val paging: PagingDto?,
    @SerializedName("response") val response: List<LeagueSeasonsDto>?,
)