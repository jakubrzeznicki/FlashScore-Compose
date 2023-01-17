package com.kuba.flashscorecompose.data.fixtures.statistics.remote.model

import com.google.gson.annotations.SerializedName

data class StatisticsDataDto(@SerializedName("response") val response: List<StatisticsTeamDto>?)