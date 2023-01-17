package com.kuba.flashscorecompose.data.fixtures.fixture.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue")
data class VenueEntity(
    @ColumnInfo(name = "city") val city: String,
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "capacity") val capacity: Int,
    @ColumnInfo(name = "surface") val surface: String,
    @ColumnInfo(name = "image") val image: String
)