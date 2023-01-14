package com.kuba.flashscorecompose.data.fixtures.lineups.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "teamId") val teamId: Int,
    @ColumnInfo(name = "grid") val grid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "pos") val pos: String,
)