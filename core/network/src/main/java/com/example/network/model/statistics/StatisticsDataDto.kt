package com.example.network.model.statistics

import com.google.gson.annotations.SerializedName

data class StatisticsDataDto(@SerializedName("response") val response: List<StatisticsTeamDto>?)
