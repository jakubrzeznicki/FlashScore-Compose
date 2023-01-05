package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName

data class FixtureDto(
    @SerializedName("fixture") val fixture: FixtureInfoDto?,
    @SerializedName("goals") val goals: GoalsDto?,
    @SerializedName("league") val league: LeagueFixtureDto?,
    @SerializedName("score") val score: ScoreDto?,
    @SerializedName("teams") val teams: TeamsDto?
)