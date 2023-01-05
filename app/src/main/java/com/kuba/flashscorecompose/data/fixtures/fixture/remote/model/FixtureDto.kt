package com.kuba.flashscorecompose.data.fixtures.fixture.remote.model

import com.google.gson.annotations.SerializedName
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDto

data class FixtureDto(
    @SerializedName("fixture") val fixture: FixtureInfoDto?,
    @SerializedName("goals") val goals: GoalsDto?,
    @SerializedName("league") val league: LeagueDto?,
    @SerializedName("score") val score: ScoreDto?,
    @SerializedName("teams") val teams: TeamsDto?
)