package com.example.network.model.statistics

import com.example.network.model.team.TeamDto
import com.google.gson.annotations.SerializedName

data class StatisticsTeamDto(
    @SerializedName("statistics") val statistics: List<StatisticDto>?,
    @SerializedName("team") val team: TeamDto?
)
