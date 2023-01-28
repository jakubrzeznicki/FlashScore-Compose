package com.kuba.flashscorecompose.data.fixtures.statistics.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.team.information.remote.model.TeamDto

data class StatisticsTeamDto(
    @SerializedName("statistics") val statistics: List<StatisticDto>?,
    @SerializedName("team") val team: TeamDto?
)