package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.*

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "fixture_info")
data class FixtureInfoEntity(
    @ColumnInfo(name = "date") val date: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "referee") val referee: String,
    @Embedded(prefix = "status_") val status: StatusEntity,
    @ColumnInfo(name = "timestamp") val timestamp: Int,
    @ColumnInfo(name = "timezone") val timezone: String,
    @Embedded(prefix = "venue_") val venue: VenueEntity,
    @Embedded(prefix = "periods_") val periods: PeriodsEntity
)