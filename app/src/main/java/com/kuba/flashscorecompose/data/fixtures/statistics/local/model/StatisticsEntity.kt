package com.kuba.flashscorecompose.data.fixtures.statistics.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kuba.flashscorecompose.data.fixtures.fixture.local.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "statistics")
data class StatisticsEntity(
    @PrimaryKey @ColumnInfo(name = "statistics_id") val id: Int,
    @ColumnInfo(name = "fixture_id") val fixtureId: Int,
    @ColumnInfo(name = "statistics") val statistics: List<StatisticRowEntity>,
    @ColumnInfo(name = "team") val team: TeamEntity
)