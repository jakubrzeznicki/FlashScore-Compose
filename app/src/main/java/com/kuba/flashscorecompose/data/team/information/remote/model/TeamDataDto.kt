package com.kuba.flashscorecompose.data.team.information.remote.model

import com.google.gson.annotations.SerializedName

data class TeamDataDto(@SerializedName("response") val response: List<TeamVenueResponseDto>)