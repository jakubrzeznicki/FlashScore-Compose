package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fixture")
data class FixtureInfoEntity(
    @ColumnInfo(name = "date") val date: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "referee") val referee: String,
    @Embedded @ColumnInfo(name = "status") val status: StatusEntity,
    @ColumnInfo(name = "timestamp") val timestamp: Int,
    @ColumnInfo(name = "timezone") val timezone: String,
    @Embedded @ColumnInfo(name = "venue") val venue: VenueEntity
)