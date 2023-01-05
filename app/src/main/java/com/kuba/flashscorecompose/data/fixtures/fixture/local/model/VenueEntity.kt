package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue")
data class VenueEntity(
    @ColumnInfo(name = "city") val city: String,
    @PrimaryKey @ColumnInfo(name = "venue_id") val id: Int,
    @ColumnInfo(name = "name") val name: String
)