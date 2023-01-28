package com.kuba.flashscorecompose.data.fixtures.lineups.local.model

import androidx.room.*
import com.kuba.flashscorecompose.data.team.information.local.model.CoachEntity
import com.kuba.flashscorecompose.data.team.information.local.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "lineup", primaryKeys = ["primary_team_id", "fixture_id"])
data class LineupEntity(
    @ColumnInfo(name = "primary_team_id") val teamId: Int,
    @ColumnInfo(name = "fixture_id") val fixtureId: Int,
    @Embedded(prefix = "coach_") val coach: CoachEntity,
    @ColumnInfo(name = "formation") val formation: String,
    @ColumnInfo(name = "start_xi") val startXI: List<PlayerEntity>,
    @ColumnInfo(name = "substitutes") val substitutes: List<PlayerEntity>,
    @Embedded(prefix = "team_") val team: TeamEntity
)