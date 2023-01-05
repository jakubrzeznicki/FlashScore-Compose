package com.kuba.flashscorecompose.data.fixtures.lineups.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jrzeznicki on 04/01/2023.
 */
@Entity(tableName = "coach")
data class CoachEntity(
    @PrimaryKey @ColumnInfo(name = "coach_id") val id: Int,
    @ColumnInfo(name = "coach_name") val name: String,
    @ColumnInfo(name = "photo") val photo: String,
)