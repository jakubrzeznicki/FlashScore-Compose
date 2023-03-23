package com.example.network.model.lineup

import com.example.network.model.team.CoachDto
import com.example.network.model.team.TeamDto
import com.google.gson.annotations.SerializedName

data class LineupDto(
    @SerializedName("coach") val coach: CoachDto?,
    @SerializedName("formation") val formation: String?,
    @SerializedName("startXI") val startXI: List<StartXIDto>?,
    @SerializedName("substitutes") val substitutes: List<SubstituteDto>?,
    @SerializedName("team") val team: TeamDto?
)
