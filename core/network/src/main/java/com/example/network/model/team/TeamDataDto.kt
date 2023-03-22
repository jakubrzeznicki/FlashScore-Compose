package com.example.network.model.team

import com.google.gson.annotations.SerializedName

data class TeamDataDto(@SerializedName("response") val response: List<TeamVenueResponseDto>)
