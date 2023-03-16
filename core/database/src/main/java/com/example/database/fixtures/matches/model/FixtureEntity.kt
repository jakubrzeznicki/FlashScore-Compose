package com.example.database.fixtures.matches.model

import androidx.room.*
import com.example.database.leagues.model.LeagueEntity
import com.example.database.teams.model.TeamEntity

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "fixture")
data class FixtureEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "h2h") val h2h: String,
    @Embedded(prefix = "fixture_info_") val fixture: FixtureInfoEntity,
    @Embedded(prefix = "goals_") val goals: GoalsEntity,
    @Embedded(prefix = "league_") val league: LeagueEntity,
    @Embedded(prefix = "score_") val score: ScoreEntity,
    @Embedded(prefix = "home_team_") val homeTeam: TeamEntity,
    @Embedded(prefix = "away_team_") val awayTeam: TeamEntity
)
