package com.kuba.flashscorecompose.data.fixtures.statistics.local.model

import androidx.room.*
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "statistics")
data class StatisticsEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "fixture_id") val fixtureId: Int,
    @ColumnInfo(name = "statistics") val statistics: List<StatisticRowEntity>,
    @Embedded(prefix = "team_") val team: TeamEntity
)