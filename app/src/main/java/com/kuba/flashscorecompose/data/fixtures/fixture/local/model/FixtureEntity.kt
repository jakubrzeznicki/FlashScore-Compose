package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity

data class FixtureEntity(
    @Embedded @ColumnInfo(name =  "current_round") val currentRound: CurrentRoundEntity,
    @ColumnInfo(name = "h2h") val h2h: String?,
    @ColumnInfo(name = "fixture") val fixture: FixtureInfoEntity,
    @Embedded @ColumnInfo(name = "goals") val goals: GoalsEntity,
    @Embedded @ColumnInfo(name = "league") val league: LeagueFixtureEntity,
    @Embedded @ColumnInfo(name = "score") val score: ScoreEntity,
    @Embedded @ColumnInfo(name = "home_team") val homeTeam: TeamEntity,
    @Embedded @ColumnInfo(name = "away_team") val awayTeam: TeamEntity
)