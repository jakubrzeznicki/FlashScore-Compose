package com.example.database.fixtures.statistics.model

import androidx.room.*
import com.example.database.teams.model.TeamEntity

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "statistics", primaryKeys = ["primary_team_id", "fixture_id"])
data class StatisticsEntity(
    @ColumnInfo(name = "primary_team_id") val teamId: Int,
    @ColumnInfo(name = "fixture_id") val fixtureId: Int,
    @ColumnInfo(name = "statistics") val statistics: List<StatisticRowEntity>,
    @Embedded(prefix = "team_") val team: TeamEntity,
    @ColumnInfo(name = "is_home") val isHome: Boolean
)
