package com.example.database.fixtures.lineups.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.example.database.players.model.PlayerEntity
import com.example.database.teams.model.CoachEntity
import com.example.database.teams.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
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
