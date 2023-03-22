package com.example.network.model.team

import com.google.gson.annotations.SerializedName

data class CoachDataDto(@SerializedName("response") val response: List<CoachDto>)
