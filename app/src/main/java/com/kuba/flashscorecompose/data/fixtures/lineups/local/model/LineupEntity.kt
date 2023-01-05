package com.kuba.flashscorecompose.data.fixtures.lineups.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "lineup")
data class LineupEntity(
    @PrimaryKey @ColumnInfo(name = "lineup_id") val id: Int,
    @ColumnInfo(name = "fixture_id") val fixtureId: Int,
    @Embedded @ColumnInfo(name = "coach") val coach: CoachEntity,
    @ColumnInfo(name = "formation") val formation: String,
    @ColumnInfo(name = "start_xi") val startXI: List<PlayerEntity>,
    @ColumnInfo(name = "substitutes") val substitutes: List<PlayerEntity>,
    @Embedded @ColumnInfo(name = "team") val team: TeamEntity,
)