package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.*
import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "fixture")
data class FixtureEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @Embedded(prefix = "current_round_") val currentRound: CurrentRoundEntity,
    @ColumnInfo(name = "h2h") val h2h: String?,
    @ColumnInfo(name = "date") val date: String?,
    @Embedded(prefix = "fixture_info_") val fixture: FixtureInfoEntity,
    @Embedded(prefix = "goals_") val goals: GoalsEntity,
    @Embedded(prefix = "league_") val league: LeagueEntity,
    @Embedded(prefix = "score_") val score: ScoreEntity,
    @Embedded(prefix = "home_team_") val homeTeam: TeamEntity,
    @Embedded(prefix = "away_team_") val awayTeam: TeamEntity
)