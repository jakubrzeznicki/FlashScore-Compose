package com.example.network.model.fixture

import com.example.network.model.league.LeagueDto
import com.google.gson.annotations.SerializedName

data class FixtureDto(
    @SerializedName("fixture") val fixture: FixtureInfoDto?,
    @SerializedName("goals") val goals: GoalsDto?,
    @SerializedName("league") val league: LeagueDto?,
    @SerializedName("score") val score: ScoreDto?,
    @SerializedName("teams") val teams: TeamsDto?
)
