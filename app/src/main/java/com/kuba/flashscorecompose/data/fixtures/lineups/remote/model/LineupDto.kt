package com.kuba.flashscorecompose.data.fixtures.lineups.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.TeamDto

data class LineupDto(
    @SerializedName("coach") val coach: CoachDto?,
    @SerializedName("formation") val formation: String?,
    @SerializedName("startXI") val startXI: List<StartXIDto>?,
    @SerializedName("substitutes") val substitutes: List<SubstituteDto>?,
    @SerializedName("team") val team: TeamDto?
)