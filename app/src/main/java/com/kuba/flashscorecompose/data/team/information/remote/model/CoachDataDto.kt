package com.kuba.flashscorecompose.data.team.information.remote.model

import com.google.gson.annotations.SerializedName

data class CoachDataDto(@SerializedName("response") val response: List<CoachDto>)