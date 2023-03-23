package com.example.database.fixtures.matches.model

import androidx.room.*
import com.example.database.teams.model.VenueEntity

@Entity(tableName = "fixture_info")
data class FixtureInfoEntity(
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "short_date") val shortDate: String,
    @ColumnInfo(name = "formatted_date") val formattedDate: String,
    @ColumnInfo(name = "year") val year: Int,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "referee") val referee: String,
    @Embedded(prefix = "status_") val status: StatusEntity,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "timezone") val timezone: String,
    @Embedded(prefix = "venue_") val venue: VenueEntity,
    @Embedded(prefix = "periods_") val periods: PeriodsEntity,
    @ColumnInfo(name = "is_live") val isLive: Boolean,
    @ColumnInfo(name = "is_started") val isStarted: Boolean
)
